oc project bb
for app in department     ; do oc delete pods,services,deployments -l app=${app} ; done
oc project cc
for app in employee       ; do oc delete pods,services,deployments -l app=${app} ; done
oc project aa
for app in organization   ; do oc delete pods,services,deployments -l app=${app} ; done
oc project default
for app in mongodb admin  ; do oc delete pods,services,deployments -l app=${app} ; done
oc project myproject
for app in api-test gateway  ; do oc delete pods,services,deployments -l app=${app} ; done