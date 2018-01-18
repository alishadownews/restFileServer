package restFileServer.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import restFileServer.model.ServerFile;
import restFileServer.model.ServerFileShort;
import restFileServer.service.FileSecvice;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.List;

@Controller
@RequestMapping( produces = "application/json; charset=utf-8" )
public class FileServerController {
    final String FileServerDirectory = "d:\\Projects\\my\\restFileServer\\target\\";
    final int MAX_COUNT_FIND = 25;

    @Autowired
    FileSecvice fileSecvice;

    @RequestMapping(value = "/find/{mask}", method = RequestMethod.GET)
    @ResponseBody
    public List<ServerFileShort> find(@PathVariable("mask") String mask){
        return fileSecvice.findFile(FileServerDirectory, mask, MAX_COUNT_FIND);
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ResponseBody
    public void get(HttpServletResponse response, @PathVariable("id") String id) throws IOException {
        ServerFile file = fileSecvice.getFileByCheckSum(FileServerDirectory, id);

        sendFileResponse(response, file.getFile(), file.getName());
    }

    private void sendFileResponse(HttpServletResponse response, byte[] file, String fileName ) throws IOException {
        String mimeType= URLConnection.guessContentTypeFromName(fileName);
        if(mimeType==null){
            System.out.println("mimetype is not detectable, will take default");
            mimeType = "application/octet-stream";
        }

        System.out.println("mimetype : " + mimeType);

        response.setContentType(mimeType);

        /* "Content-Disposition : inline" will show viewable types [like images/text/pdf/anything viewable by browser] right on browser
            while others(zip e.g) will be directly downloaded [may provide save as popup, based on your browser setting.]*/
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + fileName +"\""));

        response.setContentLength(file.length);

        InputStream inputStream = new ByteArrayInputStream(file);

        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }

    @RequestMapping(value = {"/save"}, method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = "text/html;charset=UTF-8" )
    @ResponseBody
    public boolean save ( @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        byte[] bytes = IOUtils.toByteArray(file.getInputStream());

        return fileSecvice.saveFile(FileServerDirectory, file.getName(), bytes);
    }

    @RequestMapping(value = {"delete/{id}"}, method = {RequestMethod.POST,RequestMethod.PUT})
    @ResponseBody
    public boolean delete(@PathVariable("id") String id) {
        return fileSecvice.deleteFile(FileServerDirectory, id);
    }



}
