package org.team10424102.blackserver;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.team10424102.blackserver.daos.ImageRepo;
import org.team10424102.blackserver.models.Image;
import org.team10424102.blackserver.utils.Cryptor;

import static junit.framework.TestCase.assertTrue;

public class ImageTests extends BaseTests {

    @Autowired ImageRepo imageRepo;

    @Test
    public void md5() {
        Image image = imageRepo.findOne(1L);
        String md5 = Cryptor.md5_hex(image.getData());
        assertTrue(md5.equals(image.getHash()));
    }










}
