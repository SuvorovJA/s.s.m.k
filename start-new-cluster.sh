#minishift start --vm-driver kvm --cpus 4 --memory 6gb
minishift addons enable admin-user
oc login -u system:admin
eval $(minishift oc-env)
oc policy add-role-to-user cluster-reader system:serviceaccount:myproject:default
oc policy add-role-to-user cluster-reader system:serviceaccount:default:default
kubectl create clusterrolebinding admin-default   --clusterrole=cluster-admin --serviceaccount=default:default
kubectl create clusterrolebinding admin-myproject --clusterrole=cluster-admin --serviceaccount=myproject:default
kubectl create clusterrolebinding serviceaccounts-view   --clusterrole=view  --group=system:serviceaccounts

oc create namespace aa
oc create namespace bb
oc create namespace cc
oc policy add-role-to-user cluster-reader system:serviceaccount:aa:default
oc policy add-role-to-user cluster-reader system:serviceaccount:bb:default
oc policy add-role-to-user cluster-reader system:serviceaccount:cc:default
kubectl create clusterrolebinding admin-aa --clusterrole=cluster-admin --serviceaccount=aa:default
kubectl create clusterrolebinding admin-bb --clusterrole=cluster-admin --serviceaccount=bb:default
kubectl create clusterrolebinding admin-cc --clusterrole=cluster-admin --serviceaccount=cc:default

oc project default
oc adm policy add-role-to-user admin developer
oc project aa
oc adm policy add-role-to-user admin developer
oc project bb
oc adm policy add-role-to-user admin developer
oc project cc
oc adm policy add-role-to-user admin developer
oc project myproject

minishift addons apply registry-route
eval $(minishift docker-env) 
eval $(minishift oc-env)
oc login -u developer -p developer
oc policy add-role-to-user registry-viewer developer
oc policy add-role-to-user registry-editor developer
docker login -u developer -p `oc whoami -t` $(minishift openshift registry) 


export CLUSTER_IP=$(minishift ip)
echo "set in hosts: $(minishift ip) microservices.info"
minishift console
