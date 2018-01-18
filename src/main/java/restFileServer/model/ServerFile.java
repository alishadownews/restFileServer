package restFileServer.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
public class ServerFile extends ServerFileShort implements Serializable {
    private byte file[];


    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public ServerFile(String id, String name, byte[] file){
        super(id, name);
        this.file= file;
    }

    @Override
    public String toString() {
        return "ServerFile{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                '}';
    }
}
