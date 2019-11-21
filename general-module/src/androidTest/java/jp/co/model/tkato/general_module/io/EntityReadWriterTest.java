package jp.co.model.tkato.general_module.io;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.MediumTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

@MediumTest // 1,000 ms 未満で終わるようなテスト
@RunWith(AndroidJUnit4.class)
public class EntityReadWriterTest {

    // region テスト用エンティティ

    @SuppressWarnings("WeakerAccess")
    public static class TestEntity {

        public static Integer testUuid   = 2432;
        public static String  testTitle  = "fdiafo";
        public static Boolean testIsHoge = false;

        public static TestEntity create() {
            System.out.println("creaate");
            return new TestEntity(testUuid, testTitle, testIsHoge);
        }

        @JsonProperty private Integer uuid;
        @JsonProperty private String  title;
        @JsonProperty private Boolean isHoge;

        public Integer getUuid() {
            return uuid;
        }

        public String getTitle() {
            return title;
        }

        @JsonProperty("isHoge")
        public Boolean isHoge() {
            return isHoge;
        }

        public TestEntity(Integer uuid, String title, Boolean isHoge) {
            this.uuid = uuid;
            this.title = title;
            this.isHoge = isHoge;
        }

        // 必要
        public TestEntity() {}
    }

    @SuppressWarnings({"WeakerAccess", "unused"})
    public static class TestEntity2 {

        public static Integer testUuid   = 9032;
        public static String  testTitle  = "ml;sA@";
        public static Boolean testIsHoge = true;

        public static TestEntity2 create() {
            return new TestEntity2(testUuid, testTitle, testIsHoge);
        }

        @JsonProperty private Integer uuid;
        @JsonProperty private String  title;
        @JsonProperty private Boolean isHoge;

        public Integer getUuid() {
            return uuid;
        }

        public String getTitle() {
            return title;
        }

        @JsonProperty("isHoge")
        public Boolean isHoge() {
            return isHoge;
        }

        public TestEntity2(Integer uuid, String title, Boolean isHoge) {
            this.uuid = uuid;
            this.title = title;
            this.isHoge = isHoge;
        }

        // 必要
        public TestEntity2() {}
    }

    // endregion テスト用エンティティ

    // region member / setup / tearDown

    private Context appContext;
    private EntityReadWriter instance;

    @Before
    public void setup() {
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        instance   = EntityReadWriter.instance(appContext);
    }

    @After
    public void tearDown() {
        // 保存したデータ全てを空にする
        final SharedPreferences sharedPreferences = appContext.getSharedPreferences(appContext.getPackageName() + "_preferences", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();

        instance   = null;
        appContext = null;
    }

    // endregion member / setup / tearDown

    // region 通常テスト

    // region bind

    @SuppressWarnings("all")
    @Test
    public void test_bind_保存していない場合はNull() {
        final TestEntity emptyEntity = instance.bind(TestEntity.class);
        assertNull(emptyEntity);
    }

    // endregion bind

    // region put / bind

    @SuppressWarnings("all")
    @Test
    public void test_put_bind_case1_通常の使用方法() {

        final Integer resultUuid   = 0;
        final String  resultTitle  = "title hoge";
        final Boolean resultIsHoge = true;

        final TestEntity entity = new TestEntity(resultUuid, resultTitle, resultIsHoge);
        instance.put(entity).commit();


        final TestEntity bindEntity = instance.bind(TestEntity.class);

        assertNotNull(bindEntity);
        assertEquals(resultUuid,   bindEntity.getUuid());
        assertEquals(resultTitle,  bindEntity.getTitle());
        assertEquals(resultIsHoge, bindEntity.isHoge());
    }

    @SuppressWarnings("all")
    @Test
    public void test_put_bind_case2_値がNull() {

        final Integer resultUuid   = null;
        final String  resultTitle  = null;
        final Boolean resultIsHoge = null;

        final TestEntity entity = new TestEntity(resultUuid, resultTitle, resultIsHoge);
        instance.put(entity).commit();


        final TestEntity bindEntity = instance.bind(TestEntity.class);

        assertNotNull(bindEntity);
        assertNull(bindEntity.getUuid());
        assertNull(bindEntity.getTitle());
        assertNull(bindEntity.isHoge());


        final Integer resultUuid2   = 0;
        final String  resultTitle2  = "title hoge";
        final Boolean resultIsHoge2 = true;

        final TestEntity entity2 = new TestEntity(resultUuid2, resultTitle2, resultIsHoge2);
        instance.put(entity).commit();
    }

    @SuppressWarnings("all")
    @Test
    public void test_put_bind_case3_上書き() {

        final Integer resultUuid1   = 0;
        final String  resultTitle1  = "title hoge 1";
        final Boolean resultIsHoge1 = true;

        final TestEntity entity1 = new TestEntity(resultUuid1, resultTitle1, resultIsHoge1);
        instance.put(entity1).commit();
        final TestEntity bindEntity1 = instance.bind(TestEntity.class);

        assertNotNull(bindEntity1);
        assertEquals(resultUuid1,   bindEntity1.getUuid());
        assertEquals(resultTitle1,  bindEntity1.getTitle());
        assertEquals(resultIsHoge1, bindEntity1.isHoge());

        ////////

        final Integer resultUuid2   = 1;
        final String  resultTitle2  = "title hoge 2";
        final Boolean resultIsHoge2 = null;

        final TestEntity bindEntity2 = new TestEntity(resultUuid2, resultTitle2, resultIsHoge2);
        instance.put(bindEntity2).commit();

        assertNotNull(bindEntity2);
        assertEquals(resultUuid2,   bindEntity2.getUuid());
        assertEquals(resultTitle2,  bindEntity2.getTitle());
        assertEquals(resultIsHoge2, bindEntity2.isHoge());
    }

    @SuppressWarnings("all")
    @Test
    public void test_put_bind_case4_bindでEntityを取得できなかった時に代わりの値を生成() {

        final Integer resultUuid1   = 0;
        final String  resultTitle1  = "title hoge 1";
        final Boolean resultIsHoge1 = true;

        final TestEntity bindEntity1 = instance.bind(TestEntity.class, () -> new TestEntity(resultUuid1, resultTitle1, resultIsHoge1));

        assertNotNull(bindEntity1);
        assertEquals(resultUuid1,   bindEntity1.getUuid());
        assertEquals(resultTitle1,  bindEntity1.getTitle());
        assertEquals(resultIsHoge1, bindEntity1.isHoge());


        final Integer resultUuid2   = 1;
        final String  resultTitle2  = "title hoge 2";
        final Boolean resultIsHoge2 = false;

        final TestEntity bindEntity2 = instance.bind(TestEntity.class, () -> new TestEntity(resultUuid2, resultTitle2, resultIsHoge2));

        // bind した時点ではまだ put されていないので、反映されるのは 2 つ目の Entity
        assertNotNull(bindEntity2);
        assertEquals(resultUuid2,   bindEntity2.getUuid());
        assertEquals(resultTitle2,  bindEntity2.getTitle());
        assertEquals(resultIsHoge2, bindEntity2.isHoge());


        // 1 つ目の Entity を put したので、Callable の中は実行されない
        instance.put(bindEntity1).commit();
        final TestEntity bindEntity3 = instance.bind(TestEntity.class, () -> {
            fail();
            return new TestEntity(9854, "jopfpw", true);
        });

        assertNotNull(bindEntity3);
        assertEquals(resultUuid1,   bindEntity3.getUuid());
        assertEquals(resultTitle1,  bindEntity3.getTitle());
        assertEquals(resultIsHoge1, bindEntity3.isHoge());
    }

    // endregion put / bind

    // region putOnce

    @SuppressWarnings("all")
    @Test
    public void test_putOnce_case1_bind結果がNullではない場合は上書きしない() {

        final Integer resultUuid1   = 0;
        final String  resultTitle1  = "title hoge 1";
        final Boolean resultIsHoge1 = true;

        final TestEntity entity1 = new TestEntity(resultUuid1, resultTitle1, resultIsHoge1);
        instance.put(entity1).commit();
        final TestEntity bindEntity1 = instance.bind(TestEntity.class);

        assertNotNull(bindEntity1);
        assertEquals(resultUuid1,   bindEntity1.getUuid());
        assertEquals(resultTitle1,  bindEntity1.getTitle());
        assertEquals(resultIsHoge1, bindEntity1.isHoge());

        ////////

        final Integer resultUuid2   = 1;
        final String  resultTitle2  = "title hoge 2";
        final Boolean resultIsHoge2 = null;

        final TestEntity entity2 = new TestEntity(resultUuid2, resultTitle2, resultIsHoge2);
        instance.putOnce(entity2).commit();

        final TestEntity bindEntity2 = instance.bind(TestEntity.class);

        assertNotNull(bindEntity2);
        assertNotEquals(resultUuid2,   bindEntity2.getUuid());
        assertNotEquals(resultTitle2,  bindEntity2.getTitle());
        assertNotEquals(resultIsHoge2, bindEntity2.isHoge());
        assertEquals   (resultUuid1,   bindEntity2.getUuid());
        assertEquals   (resultTitle1,  bindEntity2.getTitle());
        assertEquals   (resultIsHoge1, bindEntity2.isHoge());
    }

    @SuppressWarnings("all")
    @Test
    public void test_putOnce_case2_bind結果がNullの時に初めてEntityを作成する() {

        final Integer resultUuid1   = 0;
        final String  resultTitle1  = "title hoge 1";
        final Boolean resultIsHoge1 = true;

        instance
            .putOnce(
                TestEntity.class,
                () -> new TestEntity(resultUuid1, resultTitle1, resultIsHoge1)
            )
            .commit();
        final TestEntity bindEntity1 = instance.bind(TestEntity.class);

        assertNotNull(bindEntity1);
        assertEquals(resultUuid1,   bindEntity1.getUuid());
        assertEquals(resultTitle1,  bindEntity1.getTitle());
        assertEquals(resultIsHoge1, bindEntity1.isHoge());

        ////////

        final Integer resultUuid2   = 1;
        final String  resultTitle2  = "title hoge 2";
        final Boolean resultIsHoge2 = null;

        // 1 つ目の Entity を put しているので、Callable の中は実行されない
        instance
            .putOnce(
                TestEntity.class,
                () -> {
                    fail();
                    return new TestEntity(resultUuid2, resultTitle2, resultIsHoge2);
                }
            )
            .commit();

        final TestEntity bindEntity2 = instance.bind(TestEntity.class);
        assertNotNull(bindEntity2);
        assertNotEquals(resultUuid2,   bindEntity2.getUuid());
        assertNotEquals(resultTitle2,  bindEntity2.getTitle());
        assertNotEquals(resultIsHoge2, bindEntity2.isHoge());
        assertEquals   (resultUuid1,   bindEntity2.getUuid());
        assertEquals   (resultTitle1,  bindEntity2.getTitle());
        assertEquals   (resultIsHoge1, bindEntity2.isHoge());
    }

    @SuppressWarnings("all")
    @Test
    public void test_putOnce_case3_シンタックスシュガー() {

        // bind でも使用できる
        instance
            .putOnce(TestEntity.class, TestEntity::create)
            .commit();

        final TestEntity bindEntity = instance.bind(TestEntity.class);

        assertNotNull(bindEntity);
        assertEquals(TestEntity.testUuid,   bindEntity.getUuid());
        assertEquals(TestEntity.testTitle,  bindEntity.getTitle());
        assertEquals(TestEntity.testIsHoge, bindEntity.isHoge());
    }

    // endregion putOnce

    // region clear / allClear

    @Test
    public void test_clear() {

        instance
            .putOnce(TestEntity.class, TestEntity::create)
            .commit();
        final TestEntity bindEntity1 = instance.bind(TestEntity.class);

        assertNotNull(bindEntity1);
        assertEquals(TestEntity.testUuid,   bindEntity1.getUuid());
        assertEquals(TestEntity.testTitle,  bindEntity1.getTitle());
        assertEquals(TestEntity.testIsHoge, bindEntity1.isHoge());


        instance.clear(TestEntity.class).commit();

        final TestEntity bindEntity2 = instance.bind(TestEntity.class);
        assertNull(bindEntity2);
    }

    @Test
    public void test_allClear() {

        instance
            .putOnce(TestEntity.class, TestEntity::create)
            .putOnce(TestEntity2.class, TestEntity2::create)
            .commit();

        assertNotNull(instance.bind(TestEntity.class));
        assertNotNull(instance.bind(TestEntity2.class));

        instance.allClear();

        assertNull(instance.bind(TestEntity.class));
        assertNull(instance.bind(TestEntity2.class));
    }

    // endregion clear / allClear

    // endregion 通常テスト

    // region メソッドチェーンテスト

    @SuppressWarnings("all")
    @Test
    public void test_メソッドチェーン_case1() {

        instance
            .putOnce(TestEntity.class, TestEntity::create)
            .clear(TestEntity.class)
            .putOnce(TestEntity2.class, TestEntity2::create)
            .commit();

        assertNull   (instance.bind(TestEntity.class));
        assertNotNull(instance.bind(TestEntity2.class));
    }

    @SuppressWarnings("all")
    @Test
    public void test_メソッドチェーン_case2() {

        instance
            .putOnce(TestEntity.class, TestEntity::create)
            .putOnce(TestEntity2.class, TestEntity2::create)
            .clear(TestEntity.class)
            .commit();

        assertNull   (instance.bind(TestEntity.class));
        assertNotNull(instance.bind(TestEntity2.class));
    }

    @SuppressWarnings("all")
    @Test
    public void test_メソッドチェーン_case3() {

        instance
            .clear(TestEntity.class)
            .putOnce(TestEntity.class, TestEntity::create)
            .putOnce(TestEntity2.class, TestEntity2::create)
            .commit();

        assertNotNull(instance.bind(TestEntity.class));
        assertNotNull(instance.bind(TestEntity2.class));
    }


    @SuppressWarnings("all")
    @Test
    public void test_メソッドチェーン_case4() {

        instance
            .putOnce(TestEntity.class, () -> new TestEntity(32, "dso", null))
            .put(TestEntity.create())
            .putOnce(TestEntity2.class, TestEntity2::create)
            .clear(TestEntity2.class)
            .commit();

        assertNotNull(instance.bind(TestEntity.class));
        assertNull   (instance.bind(TestEntity2.class));

        final TestEntity bindEntity = instance.bind(TestEntity.class);
        assertEquals(TestEntity.testUuid,   bindEntity.getUuid());
        assertEquals(TestEntity.testTitle,  bindEntity.getTitle());
        assertEquals(TestEntity.testIsHoge, bindEntity.isHoge());
    }

    // endregion メソッドチェーンテスト
}