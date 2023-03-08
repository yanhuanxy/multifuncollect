package com.yanhuanxy.multifunexport.fileservice.operation.read;

import com.yanhuanxy.multifunexport.fileservice.dto.operation.ReadFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yym
 * @version 1.0
 */
public abstract class Reader {
    protected static final Logger logger = LoggerFactory.getLogger(Reader.class);

    public abstract String read(ReadFile readFile);
}
