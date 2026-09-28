package jp.co.sss.lms.ct.f03_report;

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
import org.openqa.selenium.support.ui.Select;

/**
 * 結合テスト レポート機能
 * ケース09
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース09 受講生 レポート登録 入力チェック")
public class Case09 {

	//トップ画面
	final String topUrl = "http://localhost:8080/lms/";

	//コース詳細画面
	final String courseDetailUrl = "http://localhost:8080/lms/course/detail";

	//レポート登録画面
	final String reportRegistUrl = "http://localhost:8080/lms/report/regist";

	//ユーザー詳細画面
	final String userdetailUrl = "http://localhost:8080/lms/user/detail";

	//画面下部
	final String down = "10000";

	//画面上部
	final String top = "0";

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

		//ログインアカウントの確認
		WebElement userElement = webDriver.findElement(By.partialLinkText("ようこそ"));
		assertEquals("ようこそ受講生ＡＡ１さん", userElement.getText());

		//エビデンスの取得
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test03() {
		//上部メニューの「ようこそ●●さん」をクリック
		webDriver.findElement(By.partialLinkText("ようこそ")).click();

		//エビデンスの取得
		getEvidence(new Object() {
		});

		//ユーザー詳細画面に遷移できているか確認
		assertEquals(userdetailUrl, webDriver.getCurrentUrl());
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 該当レポートの「修正する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		// 該当日付の「詳細」ボタンをクリック
		scrollTo(down); //ページ下部までスクロール

		By reportBy = By.xpath("//tr[contains(., '10月2日') and contains(., '週報')]//input[@value='修正する']");
		webDriver.findElement(reportBy).click();

		//エビデンス取得
		getEvidence(new Object() {
		});

		//レポート詳細画面に遷移できているか確認
		assertEquals(reportRegistUrl, webDriver.getCurrentUrl());

	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しエラー表示：学習項目が未入力")
	void test05() {

		//学習項目
		By fieldName = By.id("intFieldName_0");
		visibilityTimeout(fieldName, 3);
		webDriver.findElement(fieldName).clear();

		//ページ下部へスクロール
		scrollTo(down);

		//提出するボタンをクリック
		webDriver.findElement(By.cssSelector("button[type='submit']")).click();

		scrollTo(top);

		//エビデンス取得
		getEvidence(new Object() {
		});

		//エラー表示の確認
		WebElement error = webDriver.findElement(By.cssSelector(".form-control.errorInput"));
		assertTrue(error.isDisplayed());

	}

	@Test
	@Order(6)
	@DisplayName("テスト06 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：理解度が未入力")
	void test06() {

		//学習項目
		By fieldName = By.id("intFieldName_0");
		visibilityTimeout(fieldName, 3);
		webDriver.findElement(fieldName).sendKeys("ITリテラシー");

		//待ち処理

		//理解度
		new Select(webDriver.findElement(By.id("intFieldValue_0"))).selectByIndex(0);

		//ページ下部へスクロール
		scrollTo(down);

		//提出するボタンをクリック
		webDriver.findElement(By.cssSelector("button[type='submit']")).click();

		scrollTo(top);

		//エビデンス取得
		getEvidence(new Object() {
		});

		//エラー表示の確認
		WebElement error = webDriver.findElement(By.cssSelector(".form-control.errorInput"));
		assertTrue(error.isDisplayed());

	}

	@Test
	@Order(7)
	@DisplayName("テスト07 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が数値以外")
	void test07() {

		//理解度
		new Select(webDriver.findElement(By.id("intFieldValue_0"))).selectByIndex(1);

		//目標の達成度
		String reportValue = "テスト";

		//上記を入力
		int targetFieldIndex = 0;
		WebElement textArea = webDriver.findElement(By.id("content_" + targetFieldIndex));
		textArea.clear();
		textArea.sendKeys(reportValue);

		//ページ下部へスクロール
		scrollTo("2000");

		//提出するボタンをクリック
		webDriver.findElement(By.cssSelector("button[type='submit']")).click();

		scrollTo(top);

		//エビデンス取得（遷移）
		getEvidence(new Object() {
		});

		//エラー表示の確認
		WebElement error = webDriver.findElement(By.cssSelector(".form-control.errorInput"));
		assertTrue(error.isDisplayed());
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が範囲外")
	void test08() {

		//目標の達成度
		String reportValue = "100";

		// 上記を入力
		int targetFieldIndex = 0;
		WebElement textArea = webDriver.findElement(By.id("content_" + targetFieldIndex));
		textArea.clear();
		textArea.sendKeys(reportValue);

		//ページ下部へスクロール
		scrollTo(down);

		//提出するボタンをクリック
		webDriver.findElement(By.cssSelector("button[type='submit']")).click();

		scrollTo(top);

		//エビデンス取得
		getEvidence(new Object() {
		});

		//エラー表示の確認
		WebElement error = webDriver.findElement(By.cssSelector(".form-control.errorInput"));
		assertTrue(error.isDisplayed());
	}

	@Test
	@Order(9)
	@DisplayName("テスト09 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度・所感が未入力")
	void test09() {

		//ページ下部へスクロール
		scrollTo(down);

		String[] reportValues = {
				"", //目標の達成度
				"", //所感
		};

		// ループで上記を入力
		for (int i = 0; i < reportValues.length; i++) {
			WebElement textArea = webDriver.findElement(By.id("content_" + i));
			textArea.clear();
			textArea.sendKeys(reportValues[i]);
		}

		//提出するボタンをクリック
		webDriver.findElement(By.cssSelector("button[type='submit']")).click();

		//ページ下部へスクロール
		scrollTo(down);

		//エビデンス取得
		getEvidence(new Object() {
		});

		//エラー表示の確認
		WebElement error = webDriver.findElement(By.cssSelector(".form-control.errorInput"));
		assertTrue(error.isDisplayed());
	}

	@Test
	@Order(10)
	@DisplayName("テスト10 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：所感・一週間の振り返りが2000文字超")
	void test10() {

		//ページ下部へスクロール
		scrollTo(down);

		String[] reportValues = {
				"1", //目標達成度
				"あ".repeat(2001), //所感
				"あ".repeat(2001) //１週間の振り返り
		};

		// ループで上記を入力
		for (int i = 0; i < reportValues.length; i++) {
			WebElement textArea = webDriver.findElement(By.id("content_" + i));
			textArea.clear();
			textArea.sendKeys(reportValues[i]);
		}

		//提出するボタンをクリック
		webDriver.findElement(By.cssSelector("button[type='submit']")).click();

		pageLoadTimeout(10);

		//ページ下部へスクロール
		scrollTo(down);

		//エビデンス取得★いちおかしい
		getEvidence(new Object() {
		});

		//エラー表示の確認
		WebElement error = webDriver.findElement(By.cssSelector(".form-control.errorInput"));

		assertTrue(error.isDisplayed());
	}

}
