package com.example.demo.cms;

import java.util.*;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.util.MultiValueMap;

import org.springframework.web.server.ResponseStatusException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;   
import java.lang.IllegalArgumentException;

import com.example.demo.config.*;
import com.example.demo.security.*;


@RestController
public class CMSController {

    
    private CMSService cms;

   // private JSONObject userObj;
    
    @Autowired
    public CMSController(CMSService cms) {
        this.cms = cms;
    }

    /*  
    CHECK PERMISSIONS
    According to roles:
        ANALYST CAN CREATE, GET, UPDATE, DELETE
        MANAGER CAN CREATE, GET, UPDATE, DELETE, APPROVE
        --> only approved content can be displayed <--
        USER CAN ONLY GET CONTENT
    */

    @GetMapping(path="/contents/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Content getContent(@PathVariable int id) throws NotFoundException, UnauthorizedException {
        AuthorizedUser context = new AuthorizedUser();
        context.validate();
        return cms.getContent(id, context.isManager());
    }


    @GetMapping(path="/contents")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<Content> getContents() throws UnauthorizedException {
        AuthorizedUser context = new AuthorizedUser();
        context.validate();
        return cms.listContent();
    }

    @PostMapping(path="/contents")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Content addContent (@RequestBody Content content) throws UnauthorizedException {
        AuthorizedUser context = new AuthorizedUser();
        context.validate();
        return cms.addContent(content, context.isManager());
    }

    @PutMapping("/contents/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Content updateContent(@PathVariable int id, @RequestBody Content newContentInfo) throws NotFoundException, UnauthorizedException {
        AuthorizedUser context = new AuthorizedUser();
        context.validate();
        return cms.updateContent(id, newContentInfo, context.isManager());
    }


    @DeleteMapping("/contents/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void deleteBook(@PathVariable int id) throws NotFoundException, UnauthorizedException {
        AuthorizedUser context = new AuthorizedUser();
        context.validate();
        cms.deleteContent(id);
    }
    

    // @PutMapping("/content/{id}")
    // public Content isApproved(@PathVariable int id, boolean approvalValue){
    //     AuthorizedUser context = new AuthorizedUser();
    //     if (context.isManager()) {
    //         Optional<Content> contentEntity = cms.findById(id);
    //         Content content;
    //         if (!contentEntity.isPresent()) {
    //             throw new ContentNotFoundException(id);
    //         } else {
    //             content = contentEntity.get();
    //             content.setApproved(approvalValue);
    //         }
    //         return content;
    //    }
    //    else
    //         return null;
    // }


}

