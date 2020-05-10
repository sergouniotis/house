pipeline {
   agent any

   tools {
      // Install the Maven version configured as "M3" and add it to the path.
      maven "M3"
   }

   stages {
      stage('build house') {
        steps {      
          sh "mvn clean install -Dmaven.test.skip=true -Pdev"
        }
      }      
   }
}
