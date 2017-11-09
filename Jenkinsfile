@Library('deploy')
import deploy

def deployLib = new deploy()

node {
    def repo="sbtfp"
    def application = "hello-apus"
    def committer, committerEmail, changelog,pom, releaseVersion, isSnapshot, nextVersion // metadata
	def mvnHome = tool "maven-3.3.9"
	def	mvn = "${mvnHome}/bin/mvn"
	def appConfig = "nais.yaml"
  	def dockerRepo = "docker.adeo.no:5000"
  	def branch = "master"
  	def groupId = "nais"
  	def environment = 't1'
  	def zone = 'fss'
  	def namespace = 'default'

    try {

        stage("checkout") {
            git url: "ssh://git@stash.devillo.no:7999/${repo}/${application}.git"
        }

        stage("initialize") {
            pom = readMavenPom file: 'pom.xml'
            releaseVersion = pom.version.tokenize("-")[0]
            isSnapshot = pom.version.contains("-SNAPSHOT")
            committer = sh(script: 'git log -1 --pretty=format:"%an (%ae)"', returnStdout: true).trim()
            committerEmail = sh(script: 'git log -1 --pretty=format:"%ae"', returnStdout: true).trim()
            changelog = sh(script: 'git log `git describe --tags --abbrev=0`..HEAD --oneline', returnStdout: true)
        }

        stage("verify maven versions") {
            sh 'echo "Verifying that no snapshot dependencies is being used."'
            sh 'grep module pom.xml | cut -d">" -f2 | cut -d"<" -f1 > snapshots.txt'
            sh 'echo "./" >> snapshots.txt'
            sh 'while read line;do if [ "$line" != "" ];then if [ `grep SNAPSHOT $line/pom.xml | wc -l` -gt 1 ];then echo "SNAPSHOT-dependencies found. See file $line/pom.xml.";exit 1;fi;fi;done < snapshots.txt'
        }

        stage("build and test application") {
           	withEnv(['HTTP_PROXY=http://webproxy-utvikler.nav.no:8088', 'NO_PROXY=adeo.no']) {
				sh "${mvn} clean install -Djava.io.tmpdir=/tmp/${application} -B -e"
            }
        }

        stage("release version") {
            sh "${mvn} versions:set -B -DnewVersion=${releaseVersion} -DgenerateBackupPoms=false"   
            sh "${mvn} clean install -Djava.io.tmpdir=/tmp/${application} -B -e"                           
            sh "docker build --build-arg version=${releaseVersion} --build-arg app_name=${application} -t ${dockerRepo}/${application}:${releaseVersion} ."
            sh "git commit -am \"set version to ${releaseVersion} (from Jenkins pipeline)\""
            sh "git push origin master"
            sh "git tag -a ${application}-${releaseVersion} -m ${application}-${releaseVersion}"
            sh "git push --tags"
        }

        stage("publish artifact") {
            sh "${mvn} clean deploy -DskipTests -B -e"
            withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'nexusUser', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
              	sh "curl -s -F r=m2internal -F hasPom=false -F e=yaml -F g=${groupId} -F a=${application} -F " + "v=${releaseVersion} -F p=yaml -F file=@${appConfig} -u ${env.USERNAME}:${env.PASSWORD} http://maven.adeo.no/nexus/service/local/artifact/maven/content"
            }
            sh "docker push ${dockerRepo}/${application}:${releaseVersion}"
        }
        
        stage("deploy to preprod") {
            callback = "${env.BUILD_URL}input/Deploy/"
            deployLib.testCmd(releaseVersion)
            deployLib.testCmd(committer)

            def deploy = deployLib.deployNaisApp(${application}, releaseVersion, environment, zone, namespace, callback, committer).key

            try {
                timeout(time: 15, unit: 'MINUTES') {
                    input id: 'deploy', message: "Check status here:  https://jira.adeo.no/browse/${deploy}"
                }
            } catch (Exception e) {
                throw new Exception("Deploy feilet :( \n Se https://jira.adeo.no/browse/" + deploy + " for detaljer", e)

            }
        }			

        // Add test of preprod instance here

        stage("new dev version") {
            nextVersion = (releaseVersion.toInteger() + 1) + "-SNAPSHOT"
            sh "${mvn} versions:set -B -DnewVersion=${nextVersion} -DgenerateBackupPoms=false"
            sh "git commit -am \"updated to new dev-version ${nextVersion} after release by ${committer}\""
            sh "git push origin master"
        }

        hipchatSend  color: 'GREEN', message: "Jeg deployet akkurat ${application} :${releaseVersion} til Nais", textFormat: true, room: 'PAM - CV Utvikling', v2enabled: true, token: 'ZzxxzGzuY7BgKHk6dy6TJ1XqCQpAa34Zi6Tm4M2R'


    } catch (e) {
        hipchatSend color: 'RED', message: "@all ${env.JOB_NAME} failed (nice) \n \n Committer: ${committer} \n Jenkins: http://a34apvl00015.devillo.no:8080/job/pam-arena-T1/ " , textFormat: true, notify: true, room: 'PAM - CV Utvikling', v2enabled: true, token: 'ZzxxzGzuY7BgKHk6dy6TJ1XqCQpAa34Zi6Tm4M2R'
    }

}