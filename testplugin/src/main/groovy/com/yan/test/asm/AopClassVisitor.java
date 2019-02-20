package com.yan.test.asm;

import java.util.Arrays;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class AopClassVisitor extends ClassVisitor {
  public AopClassVisitor(int api, ClassVisitor cv) {
    super(api, cv);
  }

  @Override
  public void visit(int version, int access, String name, String signature, String superName,
      String[] interfaces) {
    super.visit(version, access, name, signature, superName, interfaces);
    System.out.println("AopClassVisitor       "
        + access
        + "   "
        + name
        + "   "
        + signature
        + "   "
        + superName
        + "   "
        + Arrays.asList(interfaces));
  }

  @Override public MethodVisitor visitMethod(int access, String name, String desc, String signature,
      String[] exceptions) {
    return new AopMethodVisitor(api, super.visitMethod(access, name, desc, signature, exceptions),
        access, desc, name);
  }
}
