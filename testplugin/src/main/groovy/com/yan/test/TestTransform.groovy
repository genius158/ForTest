package com.yan.test

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.yan.test.asm.AopClassVisitor
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

/**
 * code from https://github.com/liujianwj/AsmDemo/blob/master/buildsrc/src/main/groovy/com/liujian/AsmInjectTrans.groovy*/
class TestTransform extends Transform {

  @Override
  String getName() {
    return "AsmInject"
  }

  //用于指明本Transform的名字，也是代表该Transform的task的名字
  @Override
  Set<QualifiedContent.ContentType> getInputTypes() {
    return TransformManager.CONTENT_CLASS
  }

  //指明Transform的输入类型，可以作为输入过滤的手段
  @Override
  Set<? super QualifiedContent.Scope> getScopes() {
    return TransformManager.SCOPE_FULL_PROJECT
  }

  //指明Transform的作用域
  @Override
  boolean isIncremental() {
    return false
  }

  //指明是否是增量构建
  @Override
  void transform(TransformInvocation transformInvocation)
      throws TransformException, InterruptedException, IOException {
    super.transform(transformInvocation)
    // Transform的inputs有两种类型，一种是目录，一种是jar包，要分开遍历
    transformInvocation.inputs.each { TransformInput input ->
      //对类型为“文件夹”的input进行遍历
      input.directoryInputs.each { DirectoryInput directoryInput ->

        //文件夹里面包含的是我们手写的类以及R.class、BuildConfig.class以及R$XXX.class等
        if (directoryInput.file.isDirectory()) {
          // println "==== directoryInput.file = " + directoryInput.file
          directoryInput.file.eachFileRecurse { File file -> fileModify(file)
          }
        }

        // 获取output目录
        def dest = transformInvocation.outputProvider.getContentLocation(directoryInput.name,
            directoryInput.contentTypes, directoryInput.scopes,
            Format.DIRECTORY)

        System.out.println("FilePath     outputFilePath === " + dest)

        // 将input的目录复制到output指定目录
        FileUtils.copyDirectory(directoryInput.file, dest)
      }
      //对类型为jar文件的input进行遍历
      input.jarInputs.each { JarInput jarInput ->

        //jar文件一般是第三方依赖库jar文件
        // 重命名输出文件（同目录copyFile会冲突）

        def jarName = jarInput.name
        def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
        if (jarName.endsWith(".jar")) {
          jarName = jarName.substring(0, jarName.length() - 4)
        }
        System.out.println("FilePath     outputFilePath === jar " + jarName +
            "    " +
            jarInput.file +
            "     " +
            jarInput.file.listFiles()
        )

        //生成输出路径
        def dest = transformInvocation.outputProvider.getContentLocation(jarName + md5Name,
            jarInput.contentTypes, jarInput.scopes, Format.JAR)
        //将输入内容复制到输出
        FileUtils.copyFile(jarInput.file, dest)
      }
    }
  }

  static def fileModify(File file) {
    def name = file.name

    System.out.println("FilePath   directoryInput file name ==== " + name)

    //  println "==== directoryInput file name ==== " + file.getAbsolutePath()
    if (name.endsWith(".class") && !name.endsWith("R.class") &&
        !name.endsWith("BuildConfig.class") &&
        !name.contains("R\$")) {
      System.out.println("FilePath   directoryInput file name ==== " + file.getAbsolutePath())

      ClassReader classReader = new ClassReader(file.bytes)
      ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
      AopClassVisitor classVisitor = new AopClassVisitor(Opcodes.ASM5, classWriter)
      classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
      byte[] bytes = classWriter.toByteArray()
      File destFile = new File(file.parentFile.absoluteFile, name)
      //project.logger.error "==== 重新写入的位置->lastFilePath === " + destFile.getAbsolutePath()
      FileOutputStream fileOutputStream = new FileOutputStream(destFile)
      fileOutputStream.write(bytes)
      fileOutputStream.close()
    }
  }
}