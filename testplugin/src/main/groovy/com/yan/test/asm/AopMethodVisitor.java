package com.yan.test.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

class AopMethodVisitor extends AdviceAdapter {

  private final String mMethodName;
  private final String mMethodDes;

  public AopMethodVisitor(int api, MethodVisitor originMV, int access, String desc,
      String methodName) {
    super(api, originMV, access, methodName, desc);
    mMethodName = methodName;
    mMethodDes = desc;

    System.out.println("AopMethodVisitor  -------  " + mMethodDes + "    " + methodDesc);
  }

  @Override protected void onMethodEnter() {
    if ("onClick".equals(mMethodName) && "(Landroid/view/View;)V".equals(mMethodDes)) {
      mv.visitVarInsn(ALOAD, 1);
      mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/yan/fortest/LogUtils", "e",
          "(Landroid/view/View;)V", false);
      mv.visitEnd();
    }

    if ("onCreate".equals(mMethodName) && "(Landroid/os/Bundle;)V".equals(mMethodDes)) {
      mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/yan/fortest/LogUtils", "e", "()V", false);
    }
  }

  @Override public void visitMethodInsn(int i, String s, String s1, String s2, boolean b) {
    super.visitMethodInsn(i, s, s1, s2, b);
  }
}
