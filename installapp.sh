for file in kubernetes-mongo/*.yaml ; do oc apply -f "$file"; done
for file in kubernetes/*.yaml ; do oc apply -f "$file"; done
