package com.baidu.unbiz.easymapper.metadata;

import static java.lang.String.format;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.baidu.unbiz.easymapper.util.ClassUtil;

/**
 * VariableRef represents a reference to a given variable or property; it
 * contains various helper methods to properly set it's value and interrogate
 * it's underlying property or type. It also returns a properly type-safe cast
 * of it as the toString() method, so it can safely be used directly as a
 * replacement parameter for source code statements.
 *
 * @author matt.deboer@gmail.com
 */
public class VariableRef {

    protected String name;
    private Property property;
    private Type<?> type;
    private boolean nullPossible;

    public VariableRef(Property property, String name) {
        this.name = name;
        this.property = property;
        this.type = property.getType();
        this.nullPossible = !isPrimitive();
    }

    public VariableRef(Type<?> type, String name) {
        this.name = name;
        this.type = type;
        this.nullPossible = !isPrimitive();
    }

    protected String getter() {
        return property == null ? name : getGetter(property, name);
    }

    protected String setter() {
        return property == null ? name + " = %s" : getSetter(property, name);
    }

    public boolean isReadable() {
        return getter() != null;
    }

    public boolean isAssignable() {
        return setter() != null;
    }

    public Class<?> rawType() {
        return type.getRawType();
    }

    /**
     * @return the Property (if any) associated with this VariableRef
     */
    public Property property() {
        return property;
    }

    public Type<?> type() {
        return type;
    }

    public boolean isPrimitive() {
        return type.isPrimitive();
    }

    public boolean isArray() {
        return property != null ? property.isArray() : type.getRawType().isArray();
    }

    public boolean isCollection() {
        return property != null ? property.isCollection() : Collection.class.isAssignableFrom(rawType());
    }

    public boolean isList() {
        return property != null ? property.isList() : List.class.isAssignableFrom(rawType());
    }

    public boolean isSet() {
        return property != null ? property.isSet() : Set.class.isAssignableFrom(rawType());
    }

    public boolean isMap() {
        return property != null ? property.isMap() : Map.class.isAssignableFrom(rawType());
    }

    public boolean isMapEntry() {
        return Entry.class.isAssignableFrom(rawType());
    }

    public boolean isWrapper() {
        return type.isPrimitiveWrapper();
    }

    public String wrapperTypeName() {
        return ClassUtil.getWrapperType(rawType()).getCanonicalName();
    }

    public VariableRef elementRef(String name) {
        return new VariableRef(elementType(), name);
    }

    public String elementTypeName() {
        return elementType() != null ? elementType().getCanonicalName() : null;
    }

    public Type<?> elementValueType() {
        if (type.getRawType().isArray()) {
            return type.getComponentType();
        } else if (type.isMap()) {
            return type.getNestedType(1);
        } else {
            return type.getNestedType(0);
        }
    }

    public Type<?> elementType() {
        if (type.getRawType().isArray()) {
            return type.getComponentType();
        } else if (type.isMap()) {
            return MapEntry.concreteEntryType((Type<Map<Object, Object>>) type);
        } else {
            return property != null ? property.getElementType() : ((Type<?>) type.getActualTypeArguments()[0]);
        }
    }

    public String typeName() {
        return type.getCanonicalName();
    }

    public String asWrapper() {
        String ref = getter();
        if (isPrimitive()) {
            ref = ClassUtil.getWrapperType(rawType()).getCanonicalName() + ".valueOf(" + ref + ")";
        }
        return ref;
    }

    /**
     * Generates code to perform assignment to this VariableRef.
     *
     * @param value
     * @param replacements
     *
     * @return
     */
    public String assign(String value, Object... replacements) {
        if (setter() != null) {
            return assignIfPossible(value, replacements);
        } else {
            throw new IllegalArgumentException("Attempt was made to generate assignment/setter code for [" + name + "."
                    + (property != null ? property : type) + "] which has no setter/assignment method");
        }
    }

    /**
     * Generates code to perform assignment to this VariableRef, if it is
     * assignable.
     *
     * @param value
     * @param replacements
     *
     * @return
     */
    public String assignIfPossible(String value, Object... replacements) {
        if (setter() != null) {
            String expr = format(value, replacements);
            expr = cast(expr);
            return format(setter(), expr);
        } else {
            return "";
        }
    }

    /**
     * Generates code to perform assignment to this VariableRef, if it is
     * assignable.
     *
     * @param value
     *
     * @return
     */
    public String assignIfPossible(VariableRef value) {
        if (setter() != null) {
            return format(setter(), cast(value));
        } else {
            return "";
        }
    }

    public String cast(VariableRef ref) {
        return cast(ref, type());
    }

    /**
     * Returns Java code which provides a cast of the specified value to the
     * type of this property ref
     *
     * @param value
     *
     * @return
     */
    public String cast(String value) {
        return cast(value, type());
    }

    /**
     * Returns Java code which provides a cast of the specified value to the
     * type of this property ref
     *
     * @param value
     *
     * @return
     */
    protected static String cast(String value, Type<?> type) {
        String castValue = value.trim();
        String typeName = type.getCanonicalName();
        if (!"null".equals(value)) {
            if (type.isPrimitive()) {
                if (type.getRawType() == Character.TYPE) {
                    castValue = format("(\"\"+%s).charAt(0)", castValue);
                } else if (!isPrimitiveLiteral(castValue, type)) {
                    castValue =
                            format("%s.valueOf(\"\"+%s).%sValue()", type.getWrapperType().getCanonicalName(), castValue,
                                    type);
                }
            } else if (type.isPrimitiveWrapper() && isPrimitiveLiteral(castValue, type)) {
                castValue = format("%s.valueOf(%s)", type.getWrapperType().getCanonicalName(), castValue);
            } else if (!value.startsWith("(" + typeName + ")") && !value.startsWith("((" + typeName + ")")) {
                castValue = "((" + typeName + ")" + castValue + ")";
            }
        }
        return castValue;
    }

    /**
     * Returns Java code which provides a cast of the specified value to the
     * type of this property ref
     *
     * @param value
     *
     * @return
     */
    protected static String cast(VariableRef value, Type<?> type) {
        String castValue = value.toString();
        String typeName = type.getCanonicalName();

        if (type.isPrimitive()) {
            if (value.isWrapper()) {
                castValue = format("%s.%sValue()", castValue, type);
            } else if (Character.TYPE == type.getRawType() && value.type().isString()) {
                castValue = format("%s.charAt(0)", value);
            } else if (!value.isPrimitive()) {
                castValue = format("%s.valueOf(\"\"+%s).%sValue()", type.getWrapperType().getCanonicalName(), castValue,
                        typeName);
            }
        } else if (type.isPrimitiveWrapper() && value.isPrimitive()) {
            castValue = format("%s.valueOf(%s)", type.getCanonicalName(), castValue);
        } else if (type.isString() && !value.type().isString()) {
            castValue = "\"\" + " + castValue;
        } else if (!castValue.replace("(", "").startsWith(typeName)) {
            castValue = "((" + typeName + ")" + castValue + ")";
        }
        return castValue;
    }

    public String primitiveType() {
        return primitiveType(rawType());
    }

    public String primitiveType(Class<?> clazz) {
        String type = clazz.getSimpleName().toLowerCase();
        if ("integer".equals(type)) {
            type = "int";
        } else if ("character".equals(type)) {
            type = "char";
        }
        return type;
    }

    public String name() {
        return property != null && !"".equals(property.getName()) ? property.getName() : name;
    }

    public String isNull() {
        return property != null ? isNull(property, name) : getter() + " == null";
    }

    /**
     * @return true if it is possible for this variable to be null at the
     * current state within code
     */
    public boolean isNullPossible() {
        return nullPossible;
    }

    /**
     * Removes the outermost property from a nested getter expression
     *
     * @param expression
     *
     * @return
     */
    private static String unwrap(String expression) {
        if (expression.startsWith("((")) {
            expression = expression.substring(expression.indexOf(")") + 1, expression.length() - 1);
            if (expression.endsWith("]")) {
                expression = expression.substring(0, expression.lastIndexOf("["));
            } else {
                expression = expression.substring(0, expression.lastIndexOf("."));
            }
        }
        return expression;
    }

    private static String isNull(Property property, String name) {
        if (property == null) {
            return name + " == null";
        } else {
            String getterNull = getGetter(property, name) + " == null";
            if (property.isListElement()) {
                return "(" + unwrap(getGetter(property, name)) + ".size() <= " + property.getName()
                        .replaceAll("[\\[\\]]", "") + " || "
                        + getterNull + ")";
            } else if (property.isArrayElement()) {
                return "(" + unwrap(getGetter(property, name)) + ".length <= " + property.getName()
                        .replaceAll("[\\[\\]]", "") + " || "
                        + getterNull + ")";
            } else {
                return getterNull;
            }
        }
    }

    public String notNull() {
        return format("!(%s)", isNull(property, name));
    }

    public String ifNotNull() {
        return "if ( " + notNull() + ")";
    }

    public String ifNull() {
        return "if ( " + isNull() + ") ";
    }

    public String toString() {
        return getter();
    }

    protected static String getGetter(final Property property, String variableExpression) {
        if (property.getGetter() == null) {
            return null;
        }
        String var = variableExpression;
        String getter = "((" + property.getType().getCanonicalName() + ")" + var;
        if (!property.isArrayElement() && !"".equals(property.getName()) && !property.getGetter().startsWith("[")) {
            getter += "." + property.getGetter() + ")";
        } else {
            getter += property.getGetter() + ")";
        }
        return getter;
    }

    /**
     * Returns a fully type-cast setter for the property which has no reliance
     * on java generics.
     *
     * @param property           the Property for which to return the getter
     * @param variableExpression the String value to use for the variable on which the getter
     *                           is called
     *
     * @return
     */
    protected static String getSetter(final Property property, final String variableExpression) {
        if (property.getSetter() == null) {
            return null;
        }

        String var = variableExpression;
        return var + (property.isArrayElement() || "".equals(property.getName()) || property.getSetter().startsWith("[")
                              ? "" : ".") + property.getSetter();
    }

    protected static boolean isPrimitiveLiteral(String expr, Type<?> type) {
        if (type.isPrimitive()) {
            String primitiveType = type.getCanonicalName();
            if ("boolean".equals(primitiveType)) {
                return expr.matches("(true|false)");
            } else if ("char".equals(primitiveType)) {
                return expr.matches("^'\\w+'$");
            } else if ("int".equals(primitiveType) || "short".equals(primitiveType)) {
                return expr.matches("\\d+");
            } else if ("long".equals(primitiveType)) {
                return expr.matches("\\d+(l|L)?");
            } else if ("float".equals(primitiveType)) {
                return expr.matches("\\d*(\\.\\d*)?(f|F)?");
            } else if ("double".equals(primitiveType)) {
                return expr.matches("\\d+(\\.\\d*)?");
            }

        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((property == null) ? 0 : property.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        VariableRef other = (VariableRef) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (property == null) {
            if (other.property != null) {
                return false;
            }
        } else if (!property.equals(other.property)) {
            return false;
        }
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        return true;
    }

}
