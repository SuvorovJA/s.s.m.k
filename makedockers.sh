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
cd ..

docker tag piomin/gateway:${tag} $(minishift openshift registry)/myproject/gateway:${tag}
docker tag piomin/department:${tag} $(minishift openshift registry)/myproject/department:${tag}
docker tag piomin/organization:${tag} $(minishift openshift registry)/myproject/organization:${tag}
docker tag piomin/employee:${tag} $(minishift openshift registry)/myproject/employee:${tag}
docker tag piomin/admin:${tag} $(minishift openshift registry)/myproject/admin:${tag}

docker push $(minishift openshift registry)/myproject/employee:${tag}
docker push $(minishift openshift registry)/myproject/organization:${tag}
docker push $(minishift openshift registry)/myproject/gateway:${tag}
docker push $(minishift openshift registry)/myproject/department:${tag}
docker push $(minishift openshift registry)/myproject/admin:${tag}
