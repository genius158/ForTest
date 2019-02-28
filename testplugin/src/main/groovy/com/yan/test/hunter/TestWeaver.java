package com.yan.test.hunter;

import com.yan.test.asm.AopClassVisitor;
import com.yan.test.hunter.asm.BaseWeaver;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

/**
 * Created by Quinn on 26/02/2017.
 * Transform to modify bytecode
 */
public class TestWeaver extends BaseWeaver {

  @Override protected ClassVisitor wrapClassWriter(ClassWriter classWriter) {
    return new AopClassVisitor(Opcodes.ASM5, classWriter);
  }
}