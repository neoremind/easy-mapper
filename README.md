# easy-mapper

[![Build Status](https://travis-ci.org/neoremind/easy-mapper.svg?branch=master)](https://travis-ci.org/neoremind/easy-mapper)
[![Coverage Status](https://coveralls.io/repos/github/neoremind/easy-mapper/badge.svg?branch=master)](https://coveralls.io/github/neoremind/easy-mapper?branch=master)
[![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](http://www.apache.org/licenses/LICENSE-2.0)

Easy mapper is a simple, light-weight tool for Java bean mapping

```
+- org.slf4j:slf4j-api:jar:1.7.7:compile
+- org.slf4j:slf4j-log4j12:jar:1.7.7:compile
|  \- log4j:log4j:jar:1.2.17:compile
+- org.javassist:javassist:jar:3.18.1-GA:compile
```

```
-------------------------------------
| Create object number:   10000      |
-------------------------------------
|     Framework     |    time cost   |
-------------------------------------
|       Easy mapper |      44ms      |
|  Cglib beancopier |       7ms      |
|         BeanUtils |     248ms      |
|     PropertyUtils |     129ms      |
|  Spring BeanUtils |      95ms      |
|             Dozer |     772ms      |
-------------------------------------
```

```
-------------------------------------
| Create object number:  100000      |
-------------------------------------
|     Framework     |    time cost   |
-------------------------------------
|       Easy mapper |     305ms      |
|  Cglib beancopier |      18ms      |
|         BeanUtils |    1373ms      |
|     PropertyUtils |     491ms      |
|  Spring BeanUtils |     274ms      |
|             Dozer |    2081ms      |
-------------------------------------
```

```
-------------------------------------
| Create object number: 1000000      |
-------------------------------------
|     Framework     |    time cost   |
-------------------------------------
|       Easy mapper |     554ms      |
|  Cglib beancopier |      32ms      |
|         BeanUtils |    4210ms      |
|     PropertyUtils |    4386ms      |
|  Spring BeanUtils |     367ms      |
|             Dozer |    6319ms      |
-------------------------------------
```