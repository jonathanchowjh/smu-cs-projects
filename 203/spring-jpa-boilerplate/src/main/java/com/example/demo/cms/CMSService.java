package com.example.demo.cms;

import java.util.*;
import org.springframework.stereotype.Service;
import com.example.demo.config.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class CMSService {
    private CMSRepository cms;
    @Autowired
    public CMSService(CMSRepository cms){
        this.cms = cms;
    }

    public List<Content> listContent() {
        List<Content> buffer = cms.findAll();
        for (int i =0; i< buffer.size(); i++){
            if (!buffer.get(i).getApproved()){
                buffer.remove(i);
            }
        }
        return buffer;
    }

    public Content getContent(int id, boolean isManager) throws NotFoundException {
        Optional<Content> contentEntity = cms.findById(id);
        // System.out.println("GET CONTENT | " + id + " | " + contentEntity.isPresent());
        if (!contentEntity.isPresent()) throw new NotFoundException("content not found");
        Content content = contentEntity.get();
        // System.out.println(content);
        if (!content.getApproved() && !isManager) throw new NotFoundException("content not found");
        return content;
    }
    
    public Content addContent(Content content, boolean isManager) {
        if (isManager) return cms.save(content);
        content.setApproved(false);
        return cms.save(content);
    }

    public Content updateContent(int id, Content content, boolean isManager) throws NotFoundException {
        Optional<Content> contentEntity = cms.findById(id);
        if (!contentEntity.isPresent()) throw new NotFoundException("content not found");
        content.setId(id);
        if (isManager) return cms.save(content);
        Content existingContent = contentEntity.get();
        content.setApproved(existingContent.getApproved());
        return cms.save(content);
    }

    public void deleteContent(int id) throws NotFoundException, IllegalArgumentException {
        Optional<Content> contentEntity = cms.findById(id);
        if (!contentEntity.isPresent()) throw new NotFoundException("content not present");
        cms.deleteById(id);
    }
}  