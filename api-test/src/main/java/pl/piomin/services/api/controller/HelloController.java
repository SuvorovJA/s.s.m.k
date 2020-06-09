package pl.piomin.services.api.controller;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class HelloController {

    private static final Log LOGGER = LogFactory.getLog(HelloController.class);

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private KubernetesClient kubernetesClient;

    @RequestMapping("/")
    public String hello() {
        return "Controller executed in namespace: <B>" + kubernetesClient.getNamespace() + "</b><br>" +
                "<a href=\"/services/core\">services list</a><br>" +
                "<a href=\"/services/aa\">endpoints aa</a><br>" +
                "<a href=\"/services/bb\">endpoints bb</a><br>" +
                "<a href=\"/services/cc\">endpoints cc</a><br>" +
                "<a href=\"/configmaps/myproject\">configmaps myproject</a><br>" +
                "<a href=\"/configmaps/myproject/create\">configmaps myproject create new</a><br>" +
                "<a href=\"/configmaps/aa\">configmaps aa</a><br>" +
                "<a href=\"/configmaps/bb\">configmaps bb</a><br>" +
                "<a href=\"/configmaps/cc\">configmaps cc</a><br>" +
                "<a href=\"/configmaps/alien\">configmap read alien</a><br><br>" +
                "<a href=\"/all\">all controllers in one page</a>"
                ;
    }


    @RequestMapping("/all")
    public String all() throws InterruptedException {
        StringBuilder sb = new StringBuilder();
        sb.append("Controller executed in namespace: <B>" + kubernetesClient.getNamespace())
                .append("</b><br><br>All services:");
        sb.append(servicesCore()).append("<br><br>");
        sb.append(servicesaa()).append("<br>");
        sb.append(servicesbb()).append("<br>");
        sb.append(servicescc()).append("<br><br>");
        sb.append(configmapsMyproject()).append("<br><br>");
        sb.append(configmapsCreate()).append("<br><br>after create configmap:<br>");
        sb.append(configmapsMyproject()).append("<br><br>read value from new configmap:<br>");
        sb.append(getValueConfigmap("myproject", "new-cmap", "new_key"))
                .append("<br><br>");
        sb.append(configmapsaa()).append("<br>");
        sb.append(configmapsbb()).append("<br>");
        sb.append(configmapscc()).append("<br><br>read alien configmap:<br>");
        sb.append(readConfigmap());

        return sb.toString();
    }

    @RequestMapping("/services/core")
    public String servicesCore() {
        LOGGER.info("All Accessible Services: " + String.join("; ", discoveryClient.getServices()));
        Stream<ServiceInstance> s = discoveryClient.getServices().stream().flatMap(it -> discoveryClient.getInstances(it).stream());
        StringBuilder sb = new StringBuilder();
        s.forEach(it -> sb.append("Instance: url=").append(it.getHost()).append(":").append(it.getPort())
                .append(", id=").append(it.getInstanceId())
                .append(", service=").append(it.getServiceId())
                .append("; \n<br>"));
        String instancesLists = sb.toString();
        LOGGER.info(instancesLists);
        return instancesLists;
    }

    @RequestMapping("/services/aa")
    public String servicesaa() {
        return getEndpoints("aa");
    }

    @RequestMapping("/services/bb")
    public String servicesbb() {
        return getEndpoints("bb");
    }

    @RequestMapping("/services/cc")
    public String servicescc() {
        return getEndpoints("cc");
    }

    @RequestMapping("/configmaps/aa")
    public String configmapsaa() {
        return getConfigmaps("aa");
    }

    @RequestMapping("/configmaps/alien")
    public String readConfigmap() {
        return getValueConfigmap("aa", "alien-configmap", "app.data.name");
    }


    @RequestMapping("/configmaps/bb")
    public String configmapsbb() {
        return getConfigmaps("bb");
    }

    @RequestMapping("/configmaps/cc")
    public String configmapscc() {
        return getConfigmaps("cc");
    }

    @RequestMapping("/configmaps/myproject")
    public String configmapsMyproject() {
        return getConfigmaps("myproject");
    }

    @RequestMapping("/configmaps/myproject/create")
    public String configmapsCreate() throws InterruptedException {
        return createConfigmap("myproject", "new-cmap");
    }

    private String getEndpoints(String namespace) {
        EndpointsList es = kubernetesClient.endpoints().inNamespace(namespace).list();
        String endpoints = namespace + " Endpoints: " +
                es.getItems().stream().map(e -> e.getMetadata().getName()).collect(Collectors.joining("; "));
        LOGGER.info(endpoints);
        return endpoints;
    }

    private String getConfigmaps(String namespace) {
        ConfigMapList cms = kubernetesClient.configMaps().inNamespace(namespace).list();
        String configmaps = namespace + " ConfigMaps: " +
                cms.getItems().stream().map(cm -> cm.getMetadata().getName()).collect(Collectors.joining("; "));
        LOGGER.info(configmaps);
        return configmaps;
    }

    private String createConfigmap(String namespace, String configMapName) throws InterruptedException {
        ConfigMapList cms = kubernetesClient.configMaps().inNamespace(namespace).list();
        Optional<ConfigMap> configMapOptional = cms.getItems().stream().filter(configMap -> configMap.getMetadata().getName().equals(configMapName)).findFirst();
        String result = "";
        if(configMapOptional.isPresent()){
            result = "Configmap '" + configMapName + "' already exist - delete attempt: ";
            ConfigMap configMap = configMapOptional.get();
            kubernetesClient.configMaps().inNamespace(namespace).delete(configMap);
            Thread.sleep(1200);
            result += "success;\n ";
        }
        DoneableConfigMap configMap = kubernetesClient.configMaps().inNamespace(namespace).createNew();
        ObjectMeta objectMeta = new ObjectMeta();
        objectMeta.setName(configMapName);
        configMap.withMetadata(objectMeta);
        configMap.addToData("new_key", "new_value_created_from_code");
        configMap.done();
        return result + "Success create new configmap '" + configMap.getMetadata().getName() + "'";
    }

    private String getValueConfigmap(String namespace, String configmapName, String parameterName) {
        ConfigMapList cms = kubernetesClient.configMaps().inNamespace(namespace).list();
        Optional<ConfigMap> configMapOptional = cms.getItems().stream().filter(configMap -> configMap.getMetadata().getName().equals(configmapName)).findFirst();
        String result;
        if (configMapOptional.isPresent()) {
            result = "in configmap '" + configmapName +
                    "' from namespace '" + namespace +
                    "' parameter '" + parameterName + "' has value '" +
                    configMapOptional.get().getData()
                            .getOrDefault(parameterName,
                                    "WARN: parameter not present in configmap") + "'";
        } else {
            result = "configmap '" + configmapName + "' not found in namespace '" + namespace;
        }
        LOGGER.info(result);
        return result;
    }


}
