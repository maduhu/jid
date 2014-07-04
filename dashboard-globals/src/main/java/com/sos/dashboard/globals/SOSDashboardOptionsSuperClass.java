package com.sos.dashboard.globals;

import com.sos.JSHelper.Annotations.JSOptionClass;
import com.sos.JSHelper.Annotations.JSOptionDefinition;
import com.sos.JSHelper.Exceptions.JSExceptionMandatoryOptionMissing;
import com.sos.JSHelper.Listener.JSListener;
import com.sos.JSHelper.Options.JSOptionsClass;
import com.sos.JSHelper.Options.SOSOptionBoolean;
import com.sos.JSHelper.Options.SOSOptionInFileName;
import com.sos.JSHelper.Options.SOSOptionString;
import org.apache.log4j.Logger;

import java.util.HashMap;

/**
 * \class 		nameOptionsSuperClass - title
 *
 * \brief 
 * An Options-Super-Class with all Options. This Class will be extended by the "real" Options-class (\see nameOptions.
 * The "real" Option class will hold all the things, which are normaly overwritten at a new generation
 * of the super-class.
 *
 *

 *
 * see \see C:\Dokumente und Einstellungen\Uwe Risse\Lokale Einstellungen\Temp\scheduler_editor-8766529980398533444.html for (more) details.
 * 
 * \verbatim ;
 * mechanicaly created by C:\Dokumente und Einstellungen\Uwe Risse\Eigene Dateien\sos-berlin.com\jobscheduler.1.3.9\scheduler_139\config\JOETemplates\java\xsl\JSJobDoc2JSOptionSuperClass.xsl from http://www.sos-berlin.com at 20120203101939 
 * \endverbatim
 * \section OptionsTable Tabelle der vorhandenen Optionen
 * 
 * Tabelle mit allen Optionen
 * 
 * MethodName
 * Title
 * Setting
 * Description
 * IsMandatory
 * DataType
 * InitialValue
 * TestValue
 * 
 * 
 *
 * \section TestData Eine Hilfe zum Erzeugen einer HashMap mit Testdaten
 *
 * Die folgenden Methode kann verwendet werden, um f�r einen Test eine HashMap
 * mit sinnvollen Werten f�r die einzelnen Optionen zu erzeugen.
 *
 * \verbatim
 private HashMap <String, String> SetJobSchedulerSSHJobOptions (HashMap <String, String> pobjHM) {
	pobjHM.put ("		nameOptionsSuperClass.auth_file", "test");  // This parameter specifies the path and name of a user's pr
		return pobjHM;
  }  //  private void SetJobSchedulerSSHJobOptions (HashMap <String, String> pobjHM)
 * \endverbatim
 */
@JSOptionClass(name = "SOSDashboardOptionsSuperClass", description = "SOSDashboardOptionsSuperClass")
public class SOSDashboardOptionsSuperClass extends JSOptionsClass {
    /**
     * 
     */
    private static final long  serialVersionUID           = -6664326152593606058L;
    private final String       conClassName               = "SOSDashboardOptionsSuperClass";
    @SuppressWarnings("unused")
    private static Logger      logger                     = Logger.getLogger(SOSDashboardOptionsSuperClass.class);

    /**
     * \var configurationFile : 
     * 
     *
     */
    @JSOptionDefinition(name = "Hibernate_Configuration_File", description = "", key = "Hibernate_Configuration_File", type = "SOSOptionString", mandatory = false)
    public SOSOptionInFileName hibernateConfigurationFile = new SOSOptionInFileName(this, conClassName + ".Hibernate_Configuration_File", // HashMap-Key
                                                                  "Configuration File for Hibernate", // Titel
                                                                  "./hibernate_mysql.cfg.xml", // InitValue
                                                                  " ", // DefaultValue
                                                                  true // isMandatory
                                                          );
    public SOSOptionInFileName hibernateConfigFile        = hibernateConfigurationFile;
    

        
    
    public SOSOptionInFileName hibernateConfig            = (SOSOptionInFileName) hibernateConfigurationFile.SetAlias(conClassName + ".Config");

    /**
     * \var configurationFile : 
     * 
     *
     */
    @JSOptionDefinition(name = "security_server", description = "", key = "security_server", type = "SOSOptionString", mandatory = false)
    public SOSOptionString securityServer = new SOSOptionString(this, conClassName + ".SecurityServer", // HashMap-Key
                                                                  "Security Server for security rest service", // Titel
                                                                  " ", // InitValue
                                                                  " ", // DefaultValue
                                                                  false // isMandatory
                                                          );
         
    /**
     * \var configurationFile : 
     * 
     *
     */
    @JSOptionDefinition(name = "scheduler_id", description = "", key = "scheduler_id", type = "SOSOptionString", mandatory = false)
    public SOSOptionString schedulerId = new SOSOptionString(this, conClassName + ".SchedulerId", // HashMap-Key
                                                                  "scheduler_id that is assigned to this dashboard", // Titel
                                                                  " ", // InitValue
                                                                  " ", // DefaultValue
                                                                  true // isMandatory
                                                          );
         
    
    
    @JSOptionDefinition(name = "enableJobStart", description = "", key = "enableJobStart", type = "SOSOptionBoolean", mandatory = false)
    public SOSOptionBoolean    enableJobStart             = new SOSOptionBoolean(this, conClassName + ".enable_job_start", // HashMap-Key
                                                                  "Starting jobs and orders in context menu", // Titel
                                                                  "false", // InitValue
                                                                  "false", // DefaultValue
                                                                  false // isMandatory
                                                          );
    @JSOptionDefinition(name = "enable_joc", description = "", key = "enable_joc", type = "SOSOptionBoolean", mandatory = false)
    public SOSOptionBoolean    enableJOC                  = new SOSOptionBoolean(this, conClassName + ".enable_joc", // HashMap-Key
                                                                  "Show JOC", // Titel
                                                                  "false", // InitValue
                                                                  "false", // DefaultValue
                                                                  false // isMandatory
            );
     
    @JSOptionDefinition(name = "enable_reports", description = "", key = "enable_reports", type = "SOSOptionBoolean", mandatory = false)
    public SOSOptionBoolean    enableReports                  = new SOSOptionBoolean(this, conClassName + ".enable_reports", // HashMap-Key
                                                                  "Show Reports", // Titel
                                                                  "false", // InitValue
                                                                  "false", // DefaultValue
                                                                  false // isMandatory
                                                          );

    @JSOptionDefinition(name = "enable_joe", description = "", key = "enable_joe", type = "SOSOptionBoolean", mandatory = false)
    public SOSOptionBoolean    enableJOE                  = new SOSOptionBoolean(this, conClassName + ".enable_joe", // HashMap-Key
                                                                  "Show JOC", // Titel
                                                                  "false", // InitValue
                                                                  "false", // DefaultValue
                                                                  false // isMandatory
                                                          );

    @JSOptionDefinition(name = "enable_events", description = "", key = "enable_events", type = "SOSOptionBoolean", mandatory = false)
    public SOSOptionBoolean    enableEvents               = new SOSOptionBoolean(this, conClassName + ".enable_events", // HashMap-Key
                                                                  "Show Events", // Titel
                                                                  "false", // InitValue
                                                                  "false", // DefaultValue
                                                                  false // isMandatory
                                                          );

    @JSOptionDefinition(name = "enable_jobnet", description = "", key = "enable_jobnet", type = "SOSOptionBoolean", mandatory = false)
    public SOSOptionBoolean    enableJobnet               = new SOSOptionBoolean(this, conClassName + ".enable_jobnet", // HashMap-Key
                                                                  "Show JobNet", // Titel
                                                                  "false", // InitValue
                                                                  "false", // DefaultValue
                                                                  false // isMandatory
                                                          );

    public SOSOptionBoolean getEnableReports() {
        return enableReports;
    }
    
    public SOSOptionBoolean getEnableJOC() {
        return enableJOC;
    }

    public SOSOptionBoolean getEnableEvents() {
        return enableEvents;
    }

    public SOSOptionBoolean getEnableJOE() {
        return enableJOE;
    }

    public SOSOptionBoolean getEnableJobnet() {
        return enableJobnet;
    }

    public void setEnableJOC(final SOSOptionBoolean enableJOC_) {
        enableJOC = enableJOC_;
    }

    public SOSOptionBoolean getEnableJobStart() {
        return enableJobStart;
    }

    public void setEnableJobStart(final SOSOptionBoolean enableJobStart_) {
        enableJobStart = enableJobStart_;
    }

    
    
    public SOSOptionString getSecurityServer() {
        return this.securityServer;
    }
 
    public void setSecurityServer(final SOSOptionString p_securityServer) {
        this.securityServer = p_securityServer;
    }
    
    public SOSOptionString getSchedulerId() {
        return this.schedulerId;
    }
 
    public void setSchedulerId(final SOSOptionString p_schedulerId) {
        this.schedulerId = p_schedulerId;
    }
    
    
    /**
     * \brief getconfigurationFile : 
     * 
     * \details
     * 
     *
     * \return 
     *
     */
    public SOSOptionInFileName getConfigurationFile() {
        return hibernateConfigurationFile;
    }

    /**
     * \brief setconfigurationFile : 
     * 
     * \details
     * 
     *
     * @param p_configurationFile :
     */
    public void setConfigurationFile(final SOSOptionInFileName p_configurationFile) {
        this.hibernateConfigurationFile = p_configurationFile;
    }

    public SOSDashboardOptionsSuperClass() {
        objParentClass = this.getClass();
    } // public nameOptionsSuperClass

    public SOSDashboardOptionsSuperClass(JSListener pobjListener) {
        this();
        this.registerMessageListener(pobjListener);
    } // public nameOptionsSuperClass

    //

    public SOSDashboardOptionsSuperClass(HashMap<String, String> JSSettings) throws Exception {
        this();
        this.setAllOptions(JSSettings);
    } // public nameOptionsSuperClass (HashMap JSSettings)

    /**
     * \brief getAllOptionsAsString - liefert die Werte und Beschreibung aller
     * Optionen als String
     *
     * \details
     * 
     * \see toString 
     * \see toOut
     */
    private String getAllOptionsAsString() {
        @SuppressWarnings("unused")
        final String conMethodName = conClassName + "::getAllOptionsAsString";
        String strT = conClassName + "\n";
        final StringBuffer strBuffer = new StringBuffer();
        // strT += IterateAllDataElementsByAnnotation(objParentClass, this,
        // JSOptionsClass.IterationTypes.toString, strBuffer);
        // strT += IterateAllDataElementsByAnnotation(objParentClass, this, 13,
        // strBuffer);
        strT += this.toString(); // fix
        //
        return strT;
    } // private String getAllOptionsAsString ()

    /**
     * \brief setAllOptions - �bernimmt die OptionenWerte aus der HashMap
     *
     * \details In der als Parameter anzugebenden HashMap sind Schl�ssel (Name)
     * und Wert der jeweiligen Option als Paar angegeben. Ein Beispiel f�r den
     * Aufbau einer solchen HashMap findet sich in der Beschreibung dieser
     * Klasse (\ref TestData "setJobSchedulerSSHJobOptions"). In dieser Routine
     * werden die Schl�ssel analysiert und, falls gefunden, werden die
     * dazugeh�rigen Werte den Properties dieser Klasse zugewiesen.
     *
     * Nicht bekannte Schl�ssel werden ignoriert.
     *
     * \see JSOptionsClass::getItem
     *
     * @param pobjJSSettings
     * @throws Exception
     */
    public void setAllOptions(HashMap<String, String> pobjJSSettings) {
        @SuppressWarnings("unused")
        final String conMethodName = conClassName + "::setAllOptions";
        flgSetAllOptions = true;
        objSettings = pobjJSSettings;
        super.Settings(objSettings);
        super.setAllOptions(pobjJSSettings);
        flgSetAllOptions = false;
    } // public void setAllOptions (HashMap <String, String> JSSettings)

    /**
     * \brief CheckMandatory - pr�ft alle Muss-Optionen auf Werte
     *
     * \details
     * @throws Exception
     *
     * @throws Exception
     * - wird ausgel�st, wenn eine mandatory-Option keinen Wert hat
     */
    @Override
    public void CheckMandatory() throws JSExceptionMandatoryOptionMissing //
            , Exception {
        try {
            super.CheckMandatory();
        }
        catch (Exception e) {
            throw new JSExceptionMandatoryOptionMissing(e.toString());
        }
    } // public void CheckMandatory ()

    /**
     *
     * \brief CommandLineArgs - �bernehmen der Options/Settings aus der
     * Kommandozeile
     *
     * \details Die in der Kommandozeile beim Starten der Applikation
     * angegebenen Parameter werden hier in die HashMap �bertragen und danach
     * den Optionen als Wert zugewiesen.
     *
     * \return void
     *
     * @param pstrArgs
     * @throws Exception
     */
    @Override
    public void CommandLineArgs(String[] pstrArgs) {
        super.CommandLineArgs(pstrArgs);
        this.setAllOptions(super.objSettings);
    }
} // public class nameOptionsSuperClass