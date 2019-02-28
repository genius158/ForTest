package com.yan.test

import com.android.build.gradle.AppExtension
import com.yan.test.hunter.HunterTransform
import org.gradle.api.Plugin
import org.gradle.api.Project

class TestPlugin implements Plugin<Project> {

  @Override
  void apply(Project project) {

    //使用Transform实行遍历
    def android = project.extensions.getByType(AppExtension)
    android.registerTransform(new HunterTransform(project))
  }
}