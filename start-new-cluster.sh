minishift start --vm-driver hyperkit --cpus 4 --memory 6gb
minishift addons enable admin-user
oc login -u system:admin
eval $(minishift oc-env)
oc policy add-role-to-user cluster-reader system:serviceaccount:myproject:default
kubectl create clusterrolebinding admin-default --clusterrole=cluster-admin --serviceaccount=default:default
kubectl create clusterrolebinding serviceaccounts-view   --clusterrole=view  --group=system:serviceaccounts
oc create namespace a
oc create namespace b
oc create namespace c
minishift addons apply registry-route
eval $(minishift docker-env) 
eval $(minishift oc-env)
oc login -u developer -p developer
oc policy add-role-to-user registry-viewer developer
oc policy add-role-to-user registry-editor developer
docker login -u developer -p `oc whoami -t` $(minishift openshift registry) 
echo "set in hosts: $(minishift ip) microservices.info"
minishift console