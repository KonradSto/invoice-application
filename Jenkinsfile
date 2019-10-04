node{
  stage('SCM Checkout'){
    git 'https://github.com/KonradSto/invoices-application'
  }
  stage('Compile-Verify'){
    def mvn = tool name: '', type: 'maven'
    sh "${mvn}/bin/mvn clean verify"
  }

}
