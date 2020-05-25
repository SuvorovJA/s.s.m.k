oc project default
for file in kubernetes-mongo/*.yaml ; do oc apply -f "$file"; done
for file in kubernetes-admin/*.yaml ; do oc apply -f "$file"; done
oc apply -f Monitored-ConfigMap.yaml #not accessible from myproject

oc project aa
for file in kubernetes-aa/*.yaml ; do oc apply -f "$file"; done

oc project bb
for file in kubernetes-bb/*.yaml ; do oc apply -f "$file"; done

oc project cc
for file in kubernetes-cc/*.yaml ; do oc apply -f "$file"; done

oc project myproject
for file in kubernetes/*.yaml ; do oc apply -f "$file"; done
oc apply -f Monitored-ConfigMap.yaml