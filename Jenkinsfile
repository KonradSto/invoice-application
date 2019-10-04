node{
  stage('SCM Checkout'){
    git 'https://github.com/KonradSto/invoices-application'
  }
  stage('Compile-Verify'){
  bash 'mvn clean verify'
  }

}
