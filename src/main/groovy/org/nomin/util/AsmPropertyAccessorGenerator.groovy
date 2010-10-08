package org.nomin.util

import java.lang.reflect.Method
import org.nomin.core.TypeInfo
import org.objectweb.asm.*
import java.util.concurrent.ConcurrentHashMap
import java.text.MessageFormat
import org.nomin.core.NominException
import static org.objectweb.asm.Opcodes.*
import org.slf4j.*

/**
 * Generates property accessors using ASM generating facilities.
 * @author Dmitry Dobrynin
 * Created 24.05.2010 14:02:43
 */
class AsmPropertyAccessorGenerator {
  private Logger log = LoggerFactory.getLogger(AsmPropertyAccessorGenerator)
  private Map<Class, Map<String, PropertyAccessor>> cache = new ConcurrentHashMap<Class, Map<String, PropertyAccessor>>()
  private NominClassLoader classLoader = new NominClassLoader()
  private Map<Class, Class> wrappers = [(Integer.TYPE): Integer, (Long.TYPE): Long, (Float.TYPE): Float,
          (Double.TYPE) : Double, (Byte.TYPE): Byte, (Short.TYPE): Short, (Boolean.TYPE): Boolean, (Character.TYPE): Character]

  PropertyAccessor generateAccessor(String name, TypeInfo typeInfo, Method getter, Method setter) {
    Class clazz = getter ? getter.declaringClass : setter.declaringClass
    def cached = cache[clazz]
    if (cached == null) cache[clazz] = (cached = new ConcurrentHashMap<String, PropertyAccessor>())
    if (cached[name] != null) return cached[name]
    return cached[name] = classLoader.defineClass(generate(name, getter, setter)).newInstance([name, typeInfo] as Object[])
  }

  protected byte[] generate(String name, Method getter, Method setter) {
    ClassWriter cw = new ClassWriter(0);
    FieldVisitor fv;
    MethodVisitor mv;
    AnnotationVisitor av0;
    String className = getter ? getter.declaringClass.name : setter.declaringClass.name
    log.debug "Generating a property accessor for property ${className}.${name}\nGetter: ${getter}\nSetter: ${setter}"
    String paClassName = "${className.replace('.', '/')}_${name}\$\$GENERATED_BY_ASM"

    cw.visit(V1_5, ACC_PUBLIC + ACC_SUPER, paClassName, null, "java/lang/Object", ["org/nomin/util/PropertyAccessor"] as String[]);

    fv = cw.visitField(ACC_PRIVATE, "typeInfo", Type.getDescriptor(TypeInfo), null, null); fv.visitEnd();
    fv = cw.visitField(ACC_PRIVATE, "name", Type.getDescriptor(String), null, null); fv.visitEnd();
    // Generates the only constructor
    mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Ljava/lang/String;Lorg/nomin/core/TypeInfo;)V", null, null);
    mv.visitCode();
    mv.visitVarInsn(ALOAD, 0);
    mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
    mv.visitVarInsn(ALOAD, 0);
    mv.visitVarInsn(ALOAD, 1);
    mv.visitFieldInsn(PUTFIELD, paClassName, "name", "Ljava/lang/String;");
    mv.visitVarInsn(ALOAD, 0);
    mv.visitVarInsn(ALOAD, 2);
    mv.visitFieldInsn(PUTFIELD, paClassName, "typeInfo", Type.getDescriptor(TypeInfo));
    mv.visitInsn(Opcodes.RETURN);
    mv.visitMaxs(2, 3);
    mv.visitEnd();
    // getter for type info
    mv = cw.visitMethod(ACC_PUBLIC, "getTypeInfo", Type.getMethodDescriptor(PropertyAccessor.getMethod("getTypeInfo")), null, null);
    mv.visitCode();
    mv.visitVarInsn(ALOAD, 0);
    mv.visitFieldInsn(GETFIELD, paClassName, "typeInfo", Type.getDescriptor(TypeInfo));
    mv.visitInsn(Opcodes.ARETURN);
    mv.visitMaxs(1, 1);
    mv.visitEnd();
    // getter for name
    mv = cw.visitMethod(ACC_PUBLIC, "getName", Type.getMethodDescriptor(PropertyAccessor.getMethod("getName")), null, null);
    mv.visitCode();
    mv.visitVarInsn(ALOAD, 0);
    mv.visitFieldInsn(GETFIELD, paClassName, "name", Type.getDescriptor(String));
    mv.visitInsn(ARETURN);
    mv.visitMaxs(1, 1);
    mv.visitEnd();
    // get implementation
    mv = cw.visitMethod(ACC_PUBLIC, "get", Type.getMethodDescriptor(PropertyAccessor.getMethod("get", Object)), null, null);
    mv.visitCode();
    if (getter) {
      mv.visitVarInsn(ALOAD, 1)
      mv.visitTypeInsn(CHECKCAST, Type.getInternalName(getter.declaringClass))
      mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(getter.declaringClass), getter.name, Type.getMethodDescriptor(getter))
      if (getter.returnType.isPrimitive()) {
        mv.visitMethodInsn(INVOKESTATIC, Type.getInternalName(wrappers[getter.returnType]), "valueOf",
                        Type.getMethodDescriptor(Type.getType(wrappers[getter.returnType]), [Type.getType(getter.returnType)] as Type[]))
      }
      mv.visitInsn(ARETURN)
      mv.visitMaxs(2, 2)
    } else {
      mv.visitTypeInsn(NEW, Type.getInternalName(NominException));
      mv.visitInsn(DUP);
      mv.visitLdcInsn("Property {0}.{1} has no getter!");
      mv.visitInsn(ICONST_2);
      mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
      mv.visitInsn(DUP);
      mv.visitInsn(ICONST_0);
      mv.visitVarInsn(ALOAD, 1);
      Label l1 = new Label();
      mv.visitJumpInsn(IFNULL, l1);
      mv.visitVarInsn(ALOAD, 1);
      mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;");
      mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getName", "()Ljava/lang/String;");
      Label l2 = new Label();
      mv.visitJumpInsn(GOTO, l2);
      mv.visitLabel(l1);
      mv.visitLdcInsn("");
      mv.visitLabel(l2);
      mv.visitInsn(AASTORE);
      mv.visitInsn(DUP);
      mv.visitInsn(ICONST_1);
      mv.visitVarInsn(ALOAD, 0);
      mv.visitMethodInsn(INVOKEVIRTUAL, paClassName, "getName", Type.getMethodDescriptor(PropertyAccessor.getMethod("getName")));
      mv.visitInsn(AASTORE);
      mv.visitMethodInsn(INVOKESTATIC, Type.getInternalName(MessageFormat), "format", Type.getMethodDescriptor(MessageFormat.getMethod("format", String, Object[])));
      mv.visitMethodInsn(INVOKESPECIAL, Type.getInternalName(NominException), "<init>", "(Ljava/lang/String;)V");
      mv.visitInsn(ATHROW);
      mv.visitMaxs(7, 2);
    }
    mv.visitEnd();
    // set implementation
    mv = cw.visitMethod(ACC_PUBLIC, "set", Type.getMethodDescriptor(PropertyAccessor.getMethod("set", Object, Object)), null, null);
    mv.visitCode();
    if (setter) {
      mv.visitVarInsn(ALOAD, 1)
      mv.visitTypeInsn(CHECKCAST, Type.getInternalName(setter.declaringClass))
      mv.visitVarInsn(ALOAD, 2)
      def propertyClass = setter.parameterTypes[0]
      mv.visitTypeInsn(CHECKCAST,  propertyClass.isPrimitive() ?
                    Type.getInternalName(wrappers[propertyClass]) : Type.getInternalName(propertyClass))

      if (propertyClass.isPrimitive()) {
        log.debug "Setter ${setter} has primitive argument: ${setter.parameterTypes[0]}"
        def m2c = propertyClass.simpleName + "Value"
        mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(wrappers[propertyClass]), m2c,
                Type.getMethodDescriptor(Type.getType(propertyClass), new Type[0]));
      }

      mv.visitMethodInsn(INVOKEVIRTUAL, Type.getInternalName(setter.declaringClass), setter.name, Type.getMethodDescriptor(setter))
      mv.visitInsn(RETURN)
      mv.visitMaxs(3, 3)
    } else {
      mv.visitTypeInsn(NEW, Type.getInternalName(NominException));
      mv.visitInsn(DUP);
      mv.visitLdcInsn("Property {0}.{1} has no setter!");
      mv.visitInsn(ICONST_2);
      mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
      mv.visitInsn(DUP);
      mv.visitInsn(ICONST_0);
      mv.visitVarInsn(ALOAD, 1);
      Label l1 = new Label();
      mv.visitJumpInsn(IFNULL, l1);
      mv.visitVarInsn(ALOAD, 1);
      mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "getClass", "()Ljava/lang/Class;");
      mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getName", "()Ljava/lang/String;");
      Label l2 = new Label();
      mv.visitJumpInsn(GOTO, l2);
      mv.visitLabel(l1);
      mv.visitLdcInsn("");
      mv.visitLabel(l2);
      mv.visitInsn(AASTORE);
      mv.visitInsn(DUP);
      mv.visitInsn(ICONST_1);
      mv.visitVarInsn(ALOAD, 0);
      mv.visitMethodInsn(INVOKEVIRTUAL, paClassName, "getName", "()Ljava/lang/String;");
      mv.visitInsn(AASTORE);
      mv.visitMethodInsn(INVOKESTATIC, Type.getInternalName(MessageFormat), "format", Type.getMethodDescriptor(MessageFormat.getMethod("format", String, Object[])));
      mv.visitMethodInsn(INVOKESPECIAL, Type.getInternalName(NominException), "<init>", "(Ljava/lang/String;)V");
      mv.visitInsn(ATHROW);
      mv.visitMaxs(7, 3);
    }
    mv.visitEnd();

    cw.visitEnd();
    return cw.toByteArray();
  }

  static class NominClassLoader extends ClassLoader {
    def NominClassLoader(parent) { super(parent); }

    def NominClassLoader() { super(Thread.currentThread().getContextClassLoader()) }

    Class<? extends PropertyAccessor> defineClass(byte[] bytes) { (Class<? extends PropertyAccessor>) defineClass(bytes, 0, bytes.length) }
  }
}
