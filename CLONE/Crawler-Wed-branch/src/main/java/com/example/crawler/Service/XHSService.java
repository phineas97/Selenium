package com.example.crawler.Service;

import com.example.crawler.Config.Info;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service

public interface XHSService {

    Info ExtractXHS(String url) throws IOException;

}

