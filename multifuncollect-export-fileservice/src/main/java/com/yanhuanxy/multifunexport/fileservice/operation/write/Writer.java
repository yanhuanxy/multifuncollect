package com.yanhuanxy.multifunexport.fileservice.operation.write;

import com.yanhuanxy.multifunexport.fileservice.dto.operation.WriteFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * @author yym
 * @version 1.0
 */
public abstract class Writer {
    protected static final Logger logger = LoggerFactory.getLogger(Writer.class);

    public abstract void write(InputStream inputStream, WriteFile writeFile);
}
