//package common;
//
//import java.lang.reflect.Method;
//
//public class MappingMethod {
//
//    private Class<?> ownerClass;
//    private Method method;
//    private int hash;
//
//    public static MappingMethod of(Method method) {
//        MappingMethod mappingMethod = new MappingMethod();
//        mappingMethod.setOwnerClass(method.getDeclaringClass());
//        mappingMethod.setMethod(method);
//        mappingMethod.setHash(method.hashCode());
//        return mappingMethod;
//    }
//
//    public Class<?> getOwnerClass() {
//        return ownerClass;
//    }
//
//    public void setOwnerClass(Class<?> ownerClass) {
//        this.ownerClass = ownerClass;
//    }
//
//    public Method getMethod() {
//        return method;
//    }
//
//    public void setMethod(Method method) {
//        this.method = method;
//    }
//
//    public int getHash() {
//        return hash;
//    }
//
//    public void setHash(int hash) {
//        this.hash = hash;
//    }
//}
