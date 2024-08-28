package com.ericsson.sut.test.operators;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.inject.Singleton;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.handlers.RemoteFileHandler;
import com.ericsson.cifwk.taf.tools.cli.CLICommandHelper;
import com.ericsson.cifwk.utils.FileHandling;

/**
 * CloudPortalCliOperator is a Cloud Portal TAF Operator class, which allows
 * users to perform various actions using Cloud Portal Cli calls.
 *
 */
@Operator(context = Context.CLI)
@Singleton
public class CloudPortalCliOperator {

    private Host host;
    private RemoteFileHandler remote;
    private String mysql_pass = "Shr00t12";
    Logger loger = Logger.getLogger(CloudPortalCliOperator.class);

    /**
     * CloudPortalCliOperator is the constructor which instantiates a host
     * object from data in the host.properties:
     */
    public CloudPortalCliOperator() {
        host = DataHandler.getHostByName("cloudportal");
        host.setUser("root");
        host.setPass("shroot");
    }

    public int startWebInterface()
    {
        return setWebInterface("start");
    }

    public int stopWebInterface()
    {
        return setWebInterface("stop");
    }

    private int setWebInterface (String argument)
    {
        if (isLiveCloudCheck())
        {
            return 0;
        }
        CLICommandHelper cmdHelper = new CLICommandHelper(host);
        cmdHelper.simpleExec("/opt/bitnami/ctlscript.sh " + argument);

        if(argument.equals("stop"))
        {
            cmdHelper.simpleExec("echo 'flush_all' | nc localhost 11211");
        }
        return cmdHelper.getCommandExitValue();
    }

    public int enableVappDestroyApi()
    {
        return replaceStringInFile("/opt/bitnami/apache2/htdocs/app/Controller/VappsController.php","destroy_vapp_disabled_api","destroy_vapp_api");
    }

    public int disableVappDestroyApi()
    {
        return replaceStringInFile("/opt/bitnami/apache2/htdocs/app/Controller/VappsController.php","destroy_vapp_api","destroy_vapp_disabled_api");
    }

    public int enableVappRecomposeApi()
    {
        return replaceStringInFile("/opt/bitnami/apache2/htdocs/app/Controller/VappsController.php","recompose_vapp_disabled_api","recompose_vapp_api");
    }

    public int disableVappRecomposeApi()
    {
        return replaceStringInFile("/opt/bitnami/apache2/htdocs/app/Controller/VappsController.php","recompose_vapp_api","recompose_vapp_disabled_api");
    }

    public int enableVapptemplateListVmsApi()
    {
        return replaceStringInFile("/opt/bitnami/apache2/htdocs/app/Controller/VmsController.php","vapptemplate_index_disabled_api","vapptemplate_index_api");
    }

    public int disableVapptemplateListVmsApi()
    {
        return replaceStringInFile("/opt/bitnami/apache2/htdocs/app/Controller/VmsController.php","vapptemplate_index_api","vapptemplate_index_disabled_api");
    }

    public int enableVappAddToCatalogApi()
    {
        return replaceStringInFile("/opt/bitnami/apache2/htdocs/app/Controller/VappsController.php","add_to_catalog_disabled_api","add_to_catalog_api");
    }

    public int disableVappAddToCatalogApi()
    {
        return replaceStringInFile("/opt/bitnami/apache2/htdocs/app/Controller/VappsController.php","add_to_catalog_api","add_to_catalog_disabled_api");
    }

    public int enableVcliCommands()
    {
        return replaceStringInFile("/opt/bitnami/apache2/htdocs/app/Config/vcli_config.php", "host_broken_name", "hostname");
    }

    public int disableVcliCommands()
    {
        return replaceStringInFile("/opt/bitnami/apache2/htdocs/app/Config/vcli_config.php", "hostname", "host_broken_name");
    }

    public int enableSlowPowerOff()
    {
        return replaceStringInFile("/opt/bitnami/apache2/htdocs/app/Model/Vcloud.php", "# sleep for testcase goes here #", "sleep(5); # this is the sleep to make power off slow #");
    }

    public int disableSlowPowerOff()
    {
        return replaceStringInFile("/opt/bitnami/apache2/htdocs/app/Model/Vcloud.php", "sleep(5); # this is the sleep to make power off slow #", "# sleep for testcase goes here #");
    }

    public int enableSlowResizeMemory()
    {
        return replaceStringInFile("/opt/bitnami/apache2/htdocs/app/Model/Vcloud.php", "# sleep for resize memory testcase goes here #", "sleep(5); # sleep for resize memory testcase goes here #");
    }

    public int disableSlowResizeMemory()
    {
        return replaceStringInFile("/opt/bitnami/apache2/htdocs/app/Model/Vcloud.php", "sleep(5); # sleep for resize memory testcase goes here #", "# sleep for resize memory testcase goes here #");
    }

    public int enableSlowResizeCpu()
    {
        return replaceStringInFile("/opt/bitnami/apache2/htdocs/app/Model/Vcloud.php", "# sleep for resize cpu testcase goes here #", "sleep(5); # sleep for resize cpu testcase goes here #");
    }

    public int disableSlowResizeCpu()
    {
        return replaceStringInFile("/opt/bitnami/apache2/htdocs/app/Model/Vcloud.php", "sleep(5); # sleep for resize cpu testcase goes here #", "# sleep for resize cpu testcase goes here #");
    }

    private int replaceStringInFile(String file, String replaceFrom, String replaceTo)
    {
        if (isLiveCloudCheck())
        {
            return 0;
        }
        CLICommandHelper cmdHelper = new CLICommandHelper(host);
        cmdHelper.simpleExec("sed -i 's/" + replaceFrom +"/" + replaceTo + "/g' " + file);
        return cmdHelper.getCommandExitValue();
    }

    public boolean isLiveCloudCheck()
    {
        if (host.getIp().contains("10.42.34.189") || host.getIp().contains("atvcloud3") || host.getIp().contains("atvspp"))
        {
            loger.debug("ERROR: You can't run these functions towards the live cloud portal");
            return true;
        }
        return false;
    }

    public int addFileToRemoteHost (String localFile, String remoteLocation) {
        if (isLiveCloudCheck())
        {
            return 0;
        }
        remote = new RemoteFileHandler(host);
        File file = FileHandling.getFileToDeploy(localFile);
        Path filePath = Paths.get(file.getPath());
        remote.copyLocalFileToRemote(filePath.toString(), remoteLocation);
        return 1;
    }

    public int removeRemoteFileOnHost (String remoteLocation) {
        if (isLiveCloudCheck())
        {
            return 0;
        }
        remote = new RemoteFileHandler(host);
        remote.deleteRemoteFile(remoteLocation);
        return 1;
    }

    public int setProviderNewQuotaSystem(String datacenterName, boolean useNewQuotaSystem)
    {
        int useNewQuotaSystemInt = (useNewQuotaSystem) ? 1 : 0;
        String command = new String("update `cloudportal`.`provider_vdcs` JOIN  `cloudportal`.`org_vdcs` ON `provider_vdcs`.`vcd_id` = `org_vdcs`.`provider_vdc_id` SET `new_quota_system` = '" + useNewQuotaSystemInt + "' WHERE `org_vdcs`.`name` = '" + datacenterName + "';");
        return runMysqlCommand(command);
    }

    public int setOrgVdcQuotas(String datacenterName, int runningVappsQuota, int runningCpusQuota, int runningMemoryQuota, int totalVappsQuota)
    {
        String command = new String("update `cloudportal`.`org_vdcs` SET `running_tb_limit` = '" + runningVappsQuota + "', `cpu_limit` = '" + runningCpusQuota + "', `memory_limit` = '" + runningMemoryQuota + "', `stored_tb_limit` = '" + totalVappsQuota + "' WHERE `org_vdcs`.`name` = '" + datacenterName + "';");
        return runMysqlCommand(command);
    }

    public int runMysqlCommand(String command)
    {
        if (isLiveCloudCheck())
        {
            return 0;
        }

        String escaped_command = command.replaceAll("'","\"");

        CLICommandHelper cmdHelper = new CLICommandHelper(host);
        cmdHelper.simpleExec("mysql -p" + mysql_pass + " -e '" + escaped_command + "'");
        return cmdHelper.getCommandExitValue();
    }
}