package com.baidu.unbiz.easymapper.metadata;

import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isStatic;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 属性解析
 *
 * @author zhangxu
 */
public abstract class PropertyResolver {

    private final boolean includePublicFields;

    /**
     * Creates a new PropertyResolver instance
     *
     * @param includePublicFields whether public fields should be included as properties
     */
    public PropertyResolver(boolean includePublicFields) {
        this.includePublicFields = includePublicFields;
    }

    private final Map<java.lang.reflect.Type, Map<String, Property>> propertiesCache =
            new ConcurrentHashMap<java.lang.reflect.Type, Map<String, Property>>();

    public Map<String, Property> getProperties(java.lang.reflect.Type theType) {

        Map<String, Property> properties = propertiesCache.get(theType);
        if (properties == null) {
            synchronized(theType) {
                properties = propertiesCache.get(theType);
                if (properties == null) {

                    properties = new LinkedHashMap<String, Property>();
                    Type<?> referenceType;

                    if (theType instanceof Type) {
                        referenceType = (Type<?>) theType;
                    } else if (theType instanceof Class) {
                        referenceType = TypeFactory.valueOf((Class<?>) theType);
                    } else {
                        throw new IllegalArgumentException("type " + theType + " not supported.");
                    }

                    LinkedList<Class<? extends Object>> types = new LinkedList<Class<? extends Object>>();
                    types.addFirst((Class<? extends Object>) referenceType.getRawType());
                    while (!types.isEmpty()) {
                        Class<? extends Object> type = types.removeFirst();

                        collectProperties(type, referenceType, properties);

                        if (type.getSuperclass() != null && !Object.class.equals(type.getSuperclass())) {
                            types.add(type.getSuperclass());
                        }

                        List<? extends Class<? extends Object>>
                                interfaces = Arrays.<Class<? extends Object>>asList(type.getInterfaces());
                        types.addAll(interfaces);
                    }

                    if (includePublicFields) {
                        collectPublicFieldProperties(referenceType, properties);
                    }

                    propertiesCache.put(theType, Collections.unmodifiableMap(properties));
                }
            }
        }
        return properties;
    }

    /**
     * Add public non-static fields as properties
     *
     * @param referenceType the type for which to collect public field properties
     * @param properties    the collected properties for this type
     */
    protected void collectPublicFieldProperties(Type<?> referenceType, Map<String, Property> properties) {
        for (Field f : referenceType.getRawType().getFields()) {
            if (!isStatic(f.getModifiers())) {
                final Property.Builder builder = new Property.Builder();
                builder.expression(f.getName());
                builder.name(f.getName());

                Class<?> rawType = f.getType();
                Type<?> genericType = resolveGenericType(f.getGenericType(), f.getDeclaringClass(), referenceType);
                if (genericType != null && !genericType.isAssignableFrom(rawType)) {
                    builder.type(genericType);
                } else {
                    builder.type(TypeFactory.valueOf(rawType));
                }

                if (!isFinal(f.getModifiers())) {
                    builder.setter(f.getName() + " = %s");
                }

                Property existing = properties.get(f.getName());
                if (existing == null) {
                    builder.getter(f.getName());
                    properties.put(f.getName(), builder.build(this));
                } else if (existing.getSetter() == null) {
                    builder.merge(existing);
                    properties.put(f.getName(), builder.build(this));
                }
            }
        }
    }

    /**
     * Collects all properties for the specified type.
     *
     * @param type          the type for which to collect properties
     * @param referenceType the reference type for use in resolving generic parameters as
     *                      needed
     * @param properties    the properties collected for the current type
     */
    protected abstract void collectProperties(Class<?> type, Type<?> referenceType, Map<String, Property> properties);

    /**
     * Resolves the type of a property from the provided input factors.
     *
     * @param readMethod
     * @param rawType
     * @param owningType
     * @param referenceType
     *
     * @return the resolved Type of the property
     */
    public Type<?> resolvePropertyType(Method readMethod, Class<?> rawType, Class<?> owningType,
                                       Type<?> referenceType) {

        rawType = resolveRawPropertyType(rawType, readMethod);

        Type<?> resolvedGenericType = null;
        if (referenceType.isParameterized() || hasTypeParameters(owningType) || hasTypeParameters(rawType)) {

            if (readMethod != null) {
                try {
                    resolvedGenericType = resolveGenericType(
                            readMethod.getDeclaringClass().getDeclaredMethod(readMethod.getName(), new Class[0])
                                    .getGenericReturnType(),
                            owningType,
                            referenceType);
                } catch (NoSuchMethodException e) {
                    throw new IllegalStateException("readMethod does not exist", e);
                }
            }
        }

        if (resolvedGenericType == null || resolvedGenericType.isAssignableFrom(rawType)) {
            resolvedGenericType = TypeFactory.valueOf(rawType);
        }
        return resolvedGenericType;
    }

    /**
     * Attempt to resolve the generic type, using refereceType to resolve
     * TypeVariables
     *
     * @param genericType   the type to resolve
     * @param owningType    the owning type to use for lookup of type variables
     * @param referenceType the reference type to use for lookup of type variables
     *
     * @return
     */
    private Type<?> resolveGenericType(java.lang.reflect.Type genericType, Class<?> owningType, Type<?> referenceType) {
        Type<?> resolvedType = null;
        Type<?> reference = referenceType;
        do {
            Type<?> referenceInterface = null;
            if (genericType instanceof TypeVariable) {
                if (reference.isParameterized()) {
                    java.lang.reflect.Type t = reference.getTypeByVariable((TypeVariable<?>) genericType);
                    if (t != null) {
                        resolvedType = TypeFactory.valueOf(t);
                    }
                } else if (hasTypeParameters(owningType) && owningType.isInterface()) {
                    referenceInterface = reference.findInterface(TypeFactory.valueOf(owningType));
                }
            } else if (genericType instanceof ParameterizedType) {
                if (reference.isSelfOrAncestorParameterized()) {
                    resolvedType = TypeFactory.resolveValueOf((ParameterizedType) genericType, reference);
                } else if (hasTypeParameters(owningType) && owningType.isInterface()) {
                    referenceInterface = reference.findInterface(TypeFactory.valueOf(owningType));
                } else {
                    resolvedType = TypeFactory.valueOf((ParameterizedType) genericType);
                }
            }
            reference = referenceInterface != null ? referenceInterface : reference.getSuperType();
        } while (resolvedType == null && reference != TypeFactory.TYPE_OF_OBJECT);
        return resolvedType;
    }

    /**
     * Resolves the raw property type from a property descriptor; if a read
     * method is available, use it to refine the type. The results of
     * pd.getPropertyType() are sometimes inconsistent across platforms.
     *
     * @return
     */
    private Class<?> resolveRawPropertyType(Class<?> rawType, Method readMethod) {
        try {
            return (readMethod == null ? rawType : readMethod.getDeclaringClass()
                    .getDeclaredMethod(readMethod.getName(), new Class[0])
                    .getReturnType());
        } catch (Exception e) {
            return rawType;
        }
    }

    /**
     * Tests whether the specified class has type parameters either on
     * itself or on it's super-class or declared interfaces
     *
     * @param type
     *
     * @return
     */
    protected boolean hasTypeParameters(Class<?> type) {

        boolean hasTypeParams = false;
        if (type.getTypeParameters().length > 0) {
            hasTypeParams = true;
        } else {
            if (type.getGenericSuperclass() instanceof ParameterizedType) {
                hasTypeParams = true;
            } else {
                for (java.lang.reflect.Type anInterface : type.getGenericInterfaces()) {
                    if (anInterface instanceof ParameterizedType) {
                        hasTypeParams = true;
                        break;
                    }
                }
            }
        }
        return hasTypeParams;

    }

    /**
     * Processes a property, adding it to the map of properties for the owning
     * type being processed
     *
     * @param propertyName  the name of the property
     * @param propertyType  the Class of the property
     * @param readMethod    the read method for the property
     * @param writeMethod   the write method for the property
     * @param owningType    the owning type for which the properties are being resolved
     * @param referenceType a reference type to be used for resolving generic parameters
     * @param properties
     */
    protected Property processProperty(String propertyName, Class<?> propertyType, Method readMethod,
                                       Method writeMethod, Class<?> owningType,
                                       Type<?> referenceType, Map<String, Property> properties) {
        final Property.Builder builder = new Property.Builder();

        Property property = null;

        builder.expression(propertyName);
        builder.name(propertyName);

        if (readMethod != null) {
            builder.getter(readMethod.getName() + "()");
        }
        if (writeMethod != null) {
            builder.setter(writeMethod.getName() + "(%s)");
        }

        if (readMethod != null || writeMethod != null) {

            builder.type(resolvePropertyType(readMethod, propertyType, owningType, referenceType));
            property = builder.build(this);

            Property existing = properties.get(propertyName);
            if (existing == null) {
                properties.put(propertyName, property);
            } else if (existing.getType()
                    .isAssignableFrom(property.getType()) /*&& !existing.getType().equals(property.getType())*/) {
                /*
                 * The type has been refined by the generic information in a
                 * super-type
                 */
                property = builder.merge(existing).build(this);
                properties.put(propertyName, property);
            }
        }
        return property;
    }

    /**
     * Convert the first character of the provided string to uppercase.
     *
     * @param string
     *
     * @return the String with the first character converter to uppercase.
     */
    protected String capitalize(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

}
