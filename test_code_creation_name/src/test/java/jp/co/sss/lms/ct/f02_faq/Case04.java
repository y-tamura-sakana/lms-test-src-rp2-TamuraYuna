package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

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
 * 結合テスト よくある質問機能
 * ケース04
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース04 よくある質問画面への遷移")
public class Case04 {

	//トップ画面
	final String topUrl = "http://localhost:8080/lms/";

	//コース詳細画面
	final String courseDetailUrl = "http://localhost:8080/lms/course/detail";

	//ヘルプ画面
	final String helpUrl = "http://localhost:8080/lms/help";

	//よくある質問画面
	final String questionUrl = "http://localhost:8080/lms/faq";

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

	/**
	 * test04-01 トップページへアクセスし、ログイン画面が表示される
	 * */
	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {

		goTo(topUrl);

		//ログイン画面遷移の確認処理
		assertEquals(topUrl, webDriver.getCurrentUrl());

		//エビデンス取得
		getEvidence(new Object() {
		});
	}

	/**
	 * test04-02 ログインに成功する
	 * */
	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {

		//入力値の入力
		WebElement id = webDriver.findElement(By.id("loginId"));
		id.clear();
		id.sendKeys("StudentAA01");

		WebElement password = webDriver.findElement(By.id("password"));
		password.clear();
		password.sendKeys("StudentAA0101");

		//ログインボタン押下
		webDriver.findElement(By.cssSelector(".btn.btn-primary")).click();

		//画面遷移の確認
		assertEquals(courseDetailUrl, webDriver.getCurrentUrl());

		//エビデンスの取得
		getEvidence(new Object() {
		});
	}

	/**
	 * test04-03 ヘルプ画面へ遷移し、表示させる
	 * */
	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {

		//ヘルプ画面へ遷移
		WebElement menu = webDriver.findElement(By.cssSelector(".dropdown-toggle"));
		menu.click();

		WebElement help = webDriver.findElement(By.linkText("ヘルプ"));
		help.click();

		//画面遷移確認
		assertEquals(helpUrl, webDriver.getCurrentUrl());

		//エビデンス取得
		getEvidence(new Object() {
		});
	}

	/**
	 * test04-04 別タブでよくある質問画面へ遷移し、表示させる
	 * */
	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() {

		//よくある質問へ遷移
		final WebElement question = webDriver.findElement(By.linkText("よくある質問"));
		question.click();

		//タブの切り替え
		String originalWindow = webDriver.getWindowHandle();//現在のタブ
		Set<String> allWindows = webDriver.getWindowHandles();//タブのリスト
		for (String windowHandle : allWindows) {
			if (!windowHandle.equals(originalWindow)) {
				webDriver.switchTo().window(windowHandle);
				break;
			}
		}

		//画面遷移確認
		assertEquals(questionUrl, webDriver.getCurrentUrl());

		//エビデンス取得
		getEvidence(new Object() {
		});
	}

}
