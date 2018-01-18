package restFileServer.service;

import org.springframework.stereotype.Service;
import restFileServer.model.ServerFile;
import restFileServer.model.ServerFileShort;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class FileSecvice {

    public List<ServerFileShort> findFile(String path, String find, int maxCount){
        List<ServerFileShort> outList = new ArrayList<>();
        int count = 0;

        File f=new File(path);
        String[] list=f.list();     //список файлов в текущей папке
        for(String file: list){      //проверка на совпадение
            if (count >= maxCount ){
                break;
            }
            if(file.indexOf(find) > -1){
                if (! isDirectory(path+file)){
                    outList.add(new ServerFileShort(checkSumByFilePath(path+file) , file));
                    count++;
                }
            }
        }
        return outList;
    }

    public boolean isDirectory(String file){
        File tempfile=new File(file);
        System.out.println(file);
        return tempfile.isDirectory();
    }

    private static final String ALGORITHM = "SHA-1";

    public String checkSumByFilePath(String filename){
        String out = "";
        try {
            // Получаем контрольную сумму для файла в виде массива байт
            final MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            final FileInputStream fis = new FileInputStream(filename);
            byte[] dataBytes = new byte[1024];
            int bytesRead;
            while((bytesRead = fis.read(dataBytes)) > 0) {
                md.update(dataBytes, 0, bytesRead);
            }
            byte[] mdBytes = md.digest();

            // Переводим контрольную сумму в виде массива байт в
            // шестнадцатеричное представление
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < mdBytes.length; i++) {
                sb.append(Integer.toString((mdBytes[i] & 0xff) + 0x100, 16)
                        .substring(1));
            }
            out = sb.toString();
        } catch (FileNotFoundException | NoSuchAlgorithmException ex) {
            Logger.getLogger(FileSecvice.class.getName())
                    .log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileSecvice.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return out;
    }

    public String checkSumByFileBytes(byte[] fileBytes){
        String out = "";
        try {
            // Получаем контрольную сумму для файла в виде массива байт
            final MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.update(fileBytes, 0, fileBytes.length);

            byte[] mdBytes = md.digest();

            // Переводим контрольную сумму в виде массива байт в
            // шестнадцатеричное представление
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < mdBytes.length; i++) {
                sb.append(Integer.toString((mdBytes[i] & 0xff) + 0x100, 16)
                        .substring(1));
            }
            out = sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(FileSecvice.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return out;
    }


    public ServerFile getFileByCheckSum(String path, String checkSum){
        ServerFile out = null;

        File f=new File(path);
        String[] list=f.list();     //список файлов в текущей папке
        for(String file: list){      //проверка на совпадение
            if (!isDirectory(path+file)){
                if( checkSum.equals(checkSumByFilePath(path+file))){
                    out = new ServerFile(checkSumByFilePath(path+file), file, readFile(new File(path+file)));
                }
            }
        }
        return out;
    }

    public byte[] readFile( File file )
    {
        int length;
        byte[] tmp = new byte[1024];
        ByteArrayOutputStream out = new ByteArrayOutputStream( );
        InputStream in = null;
        try {
            in = new FileInputStream( file );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            while( (length = in.read( tmp )) >= 0 )
            {
                out.write( tmp, 0, length );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return out.toByteArray( );
    }


    public boolean saveFile(String path, String fileName, byte[] fileBytes) throws IOException {
        String checkSumNewFile = checkSumByFileBytes(fileBytes);
        ServerFile findFile = getFileByCheckSum(path, checkSumNewFile);
        if (findFile == null){
            FileOutputStream fos = new FileOutputStream(new File(fileName));
            fos.write(fileBytes);
            fos.close();
            return true;
        }
        return false;
    }

    public boolean deleteFile(String path, String checkSum) {
        ServerFile findFile = getFileByCheckSum(path, checkSum);
        if (findFile != null){
            File f=new File(path+findFile.getName());
            f.delete();
            return true;
        }
        return false;
    }


}
