# Deploy Custom Artifactory
To deploy a custom Java library to your private Artifactory, you can use the following steps:

Build the library: Build the Java library using a build tool such as Maven or Gradle. Ensure that the library is packaged as a JAR file.

Publish to Artifactory: Use the Artifactory REST API or a build tool plugin to publish the JAR file to your Artifactory repository.

Configure build tool: If you used a build tool plugin to publish the library, update your build configuration to include the library as a dependency.

Here's an example of how to publish a library using the Gradle Artifactory plugin:

Add the plugin to your build.gradle file:
```groovy
plugins {
    id "com.jfrog.artifactory" version "4.14.0"
}
```
Configure the plugin:
```groovy
artifactory {
  contextUrl = "${ARTIFACTORY_URL}"
  publish {
    repository {
      repoKey = 'libs-release-local'
      username = "${ARTIFACTORY_USER}"
      password = "${ARTIFACTORY_PASSWORD}"
    }
  }
}
```

Publish the library:
```
./gradlew build artifactoryPublish
```
This will build the library and publish it to your Artifactory repository. You can then include it in other projects by adding it as a dependency in the build configuration.


## Use the custom library
Once you've published the custom Java library to your Artifactory repository, you can use it in other projects by adding it as a dependency in your build configuration.

Here's an example of how to include the library as a dependency in a Gradle project:

Add the following to your build.gradle file:
```groovy

repositories {
    maven {
        url "${ARTIFACTORY_URL}/libs-release-local"
        credentials {
            username = "${ARTIFACTORY_USER}"
            password = "${ARTIFACTORY_PASSWORD}"
        }
    }
}

dependencies {
    compile group: 'com.example', name: 'library-name', version: '1.0.0'
}
```