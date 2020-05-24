export tag=$1

cd employee-service
docker build -t piomin/employee:${tag} .
cd ../organization-service
docker build -t piomin/organization:${tag} .
cd ../gateway-service
docker build -t piomin/gateway:${tag} .
cd ../department-service
docker build -t piomin/department:${tag} .
cd ../admin-service
docker build -t piomin/admin:${tag} .
cd ../api-test
docker build -t piomin/api-test:${tag} .
cd ..

oc project bb
docker tag piomin/department:${tag} $(minishift openshift registry)/bb/department:${tag}
docker push $(minishift openshift registry)/bb/department:${tag}

oc project aa
docker tag piomin/organization:${tag} $(minishift openshift registry)/aa/organization:${tag}
docker push $(minishift openshift registry)/aa/organization:${tag}

oc project cc
docker tag piomin/employee:${tag} $(minishift openshift registry)/cc/employee:${tag}
docker push $(minishift openshift registry)/cc/employee:${tag}

oc project default
docker tag piomin/admin:${tag} $(minishift openshift registry)/default/admin:${tag}
docker push $(minishift openshift registry)/default/admin:${tag}

oc project myproject
docker tag piomin/gateway:${tag} $(minishift openshift registry)/myproject/gateway:${tag}
docker tag piomin/api-test:${tag} $(minishift openshift registry)/myproject/api-test:${tag}
docker push $(minishift openshift registry)/myproject/api-test:${tag}
docker push $(minishift openshift registry)/myproject/gateway:${tag}

