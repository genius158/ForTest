package com.yan.test

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class TestPlugin implements Plugin<Project> {

  @Override
  void apply(Project project) {

    //使用Transform实行遍历
    def android = project.extensions.getByType(AppExtension)
    registerTransform(android)

//    project.tasks.create('cleanTest', CleanTestTask)

  }


  def static registerTransform(BaseExtension android) {
    TestTransform transform = new TestTransform()
    android.registerTransform(transform)
  }

//  class CleanTestTask extends DefaultTask{
//    @Inject
//    CleanTestTask() {
//      super()
//      dependsOn "lint"
//    }
//    @TaskAction
//    def testClean(){
//      System.out.println("==================")
//      System.out.println("Test Clean Task")
//      System.out.println("==================")
//    }
//  }
}