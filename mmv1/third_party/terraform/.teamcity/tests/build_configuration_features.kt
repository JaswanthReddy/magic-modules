/*
 * Copyright (c) HashiCorp, Inc.
 * SPDX-License-Identifier: MPL-2.0
 */

// This file is maintained in the GoogleCloudPlatform/magic-modules repository and copied into the downstream provider repositories. Any changes to this file in the downstream will be overwritten.

package tests

import builds.UseTeamCityGoTest
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import projects.googleCloudRootProject

class BuildConfigurationFeatureTests {
    @Test
    fun buildShouldFailOnError() {
        val root = googleCloudRootProject(testContextParameters())

        val gaProject = getSubProject(root, gaProjectName)
        val betaProject = getSubProject(root, betaProjectName)
        val projectSweeperProject = getSubProject(root, projectSweeperProjectName)

        (gaProject.subProjects + betaProject.subProjects + projectSweeperProject.subProjects).forEach{p ->
            p.buildTypes.forEach{bt ->
                assertTrue("Build '${bt.id}' should fail on errors!", bt.failureConditions.errorMessage)
            }
        }
    }

    @Test
    fun buildShouldHaveGoTestFeature() {
        val root = googleCloudRootProject(testContextParameters())

        val gaProject = getSubProject(root, gaProjectName)
        val betaProject = getSubProject(root, betaProjectName)
        val projectSweeperProject = getSubProject(root, projectSweeperProjectName)

        (gaProject.subProjects + betaProject.subProjects + projectSweeperProject.subProjects).forEach{p ->
            var exists: ArrayList<Boolean> = arrayListOf()
            p.buildTypes.forEach{bt ->
                bt.features.items.forEach { f ->
                    exists.add(f.type == "golang")
                }
            }
            if (exists.contains(false) && UseTeamCityGoTest){
                fail("Project ${p.name} contains build configurations that don't use the Go Test feature")
            }
        }
    }
}
