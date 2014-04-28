package GUI.Model;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Freddy Mesa on 27-Apr-14.
 */

@XmlRootElement(namespace = "GUI.Model")
public class XML_Database {

    public static final String databasePath = System.getProperties().getProperty("user.dir") + "\\src\\Source\\db.xml";

    @XmlElementWrapper(name = "ActivityList")
    @XmlElement(name = "Activity")
    public List<Activity> activityList;

    @XmlElementWrapper(name = "UserList")
    @XmlElement(name = "User")
    public List<User> userList;

    public XML_Database(){

    }

    public void setActivityList(List<Activity> activityList){
        this.activityList = activityList;
    }
    public void setUserList(List<User> userList){
        this.userList = userList;
    }

    public void loadDB(){
        try{
            JAXBContext context = JAXBContext.newInstance(XML_Database.class);
            Unmarshaller um = context.createUnmarshaller();
            XML_Database db = (XML_Database) um.unmarshal(new FileReader(databasePath));

            if(db.userList != null)
                this.setUserList(db.userList);
            else
                this.userList = new ArrayList<>();

            this.setActivityList(db.activityList);

        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void saveDB(){

        try{
            JAXBContext context = JAXBContext.newInstance(XML_Database.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            // Write to System.out
            m.marshal(this, System.out);

            // Write to File
            m.marshal(this, new File(XML_Database.databasePath));

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        XML_Database db = new XML_Database();

        List<Activity> activities = new ArrayList<>();
        activities.add(new Activity(Activity.eActivity.Walking.toString(),4.0));
        activities.add(new Activity(Activity.eActivity.Sitting.toString(),1.5));
        activities.add(new Activity(Activity.eActivity.Standing.toString(),1.2));
        activities.add(new Activity(Activity.eActivity.Jogging.toString(),7.0));
        activities.add(new Activity(Activity.eActivity.Upstairs.toString(),9.0));
        activities.add(new Activity(Activity.eActivity.Downstairs.toString(),3.0));

        db.setActivityList(activities);

        try {
            JAXBContext context = JAXBContext.newInstance(XML_Database.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            // Write to System.out
            m.marshal(db, System.out);

            // Write to File
            m.marshal(db, new File(XML_Database.databasePath));

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
