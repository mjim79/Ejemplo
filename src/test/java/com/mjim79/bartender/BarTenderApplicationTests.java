package com.mjim79.bartender;

import org.junit.*;
import org.junit.runner.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.context.junit4.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BarTenderApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void test() {
        BarTenderApplication.main(new String[0]);
    }

}
