package jp.co.sss.lms.ct.f01_login1;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * 結合テスト ログイン機能①
 * ケース02
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース02 受講生 ログイン 認証失敗")
public class Case02 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	/** test02-01　トップページへアクセスし、ログイン画面が表示される */
	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		webDriver.get("http://localhost:8080/lms/");
		getEvidence(new Object() {
		});
	}

	/** test02-02　DBに登録されていないユーザーでログインをし、エラーメッセージを表示 */
	@Test
	@Order(2)
	@DisplayName("テスト02 DBに登録されていないユーザーでログイン")
	void test02() {
		webDriver.get("http://localhost:8080/lms/");

		//入力値の入力
		WebElement id = webDriver.findElement(By.id("loginId"));
		id.clear();
		id.sendKeys("123456");

		WebElement password = webDriver.findElement(By.id("password"));
		password.clear();
		password.sendKeys("123456");

		//ログインボタン押下
		webDriver.findElement(By.cssSelector(".btn.btn-primary")).click();

		//エラーメッセージの確認
		WebElement errorMsg = webDriver.findElement(By.cssSelector(".help-inline.error"));
		assertEquals("* ログインに失敗しました。", errorMsg.getText());

		//エビデンスの取得
		getEvidence(new Object() {
		});
	}

}
