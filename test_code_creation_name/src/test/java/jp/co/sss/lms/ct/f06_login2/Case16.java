package jp.co.sss.lms.ct.f06_login2;

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
 * 結合テスト ログイン機能②
 * ケース16
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース16 受講生 初回ログイン 変更パスワード未入力")
public class Case16 {

	//トップ画面
	final String topUrl = "http://localhost:8080/lms/";

	//利用規約画面
	final String userAgreeUrl = "http://localhost:8080/lms/user/agreeSecurity";

	//パスワード変更画面
	final String passChangeUrl = "http://localhost:8080/lms/password/changePassword";

	//画面下部
	final String down = "10000";

	//画面上部
	final String top = "0";

	//「変更」ボタン
	static final By changeButton = By.cssSelector("button[type='submit']");

	//確認モーダル
	static final By yesButton = By.id("upd-btn");

	//現在のパスワード入力欄
	static final By currentPass = By.cssSelector("input[name='currentPassword']");

	//新しいパスワード入力欄
	static final By newPass = By.cssSelector("input[name='password']");

	//確認パスワード入力欄
	static final By confirmPass = By.cssSelector("input[name='passwordConfirm']");

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
	@DisplayName("テスト02 DBに初期登録された未ログインの受講生ユーザーでログイン")
	void test02() {
		WebElement id = webDriver.findElement(By.id("loginId"));
		id.clear();
		id.sendKeys("StudentAA02");

		WebElement password = webDriver.findElement(By.id("password"));
		password.clear();
		password.sendKeys("StudentAA02");

		//ログインボタン押下
		webDriver.findElement(By.cssSelector(".btn.btn-primary")).click();

		//画面遷移の確認
		assertEquals(userAgreeUrl, webDriver.getCurrentUrl());

		//ログインアカウントの確認
		WebElement userElement = webDriver.findElement(By.partialLinkText("ようこそ"));
		assertEquals("ようこそ受講生ＡＡ２さん", userElement.getText());

		//エビデンスの取得
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 「同意します」チェックボックスにチェックを入れ「次へ」ボタン押下")
	void test03() {
		//画面最大化
		webDriver.manage().window().maximize();
		scrollBy(down);

		//「同意する」にチェック
		webDriver.findElement(By.cssSelector("input[type='checkBox'][value='1']")).click();

		// 「次へ」ボタンをクリック
		webDriver.findElement(By.className("btn-primary")).click();

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//エビデンス取得（遷移）
		getEvidence(new Object() {
		});

		//パスワード変更画面に遷移したか確認
		assertEquals(passChangeUrl, webDriver.getCurrentUrl());

	}

	@Test
	@Order(4)
	@DisplayName("テスト04 パスワードを未入力で「変更」ボタン押下")
	void test04() {
		//「変更」ボタンをクリック
		webDriver.findElement(changeButton).click();

		//待機時間
		visibilityTimeout(yesButton, 10);

		//確認ダイアログの認証
		webDriver.findElement(yesButton).click();

		//エビデンス取得
		getEvidence(new Object() {
		});

		//エラー表示の確認
		WebElement error = webDriver.findElement(By.cssSelector(".help-inline.error"));
		assertTrue(error.isDisplayed());

	}

	@Test
	@Order(5)
	@DisplayName("テスト05 20文字以上の変更パスワードを入力し「変更」ボタン押下")
	void test05() {
		//現在のパスワードに入力
		WebElement currentPassElement = webDriver.findElement(currentPass);
		currentPassElement.click();
		currentPassElement.clear();
		currentPassElement.sendKeys("StudentAA02");

		//新しいパスワードに入力
		WebElement PassElement = webDriver.findElement(newPass);
		PassElement.click();
		PassElement.clear();
		PassElement.sendKeys("aA1".repeat(7));

		//確認パスワードに入力
		WebElement confirmPassElement = webDriver.findElement(confirmPass);
		confirmPassElement.click();
		confirmPassElement.clear();
		confirmPassElement.sendKeys("aA1".repeat(7));

		//画面下部にスクロール＆待機処理
		scrollTo(down);
		visibilityTimeout(changeButton, 10);

		//「変更」ボタンクリック
		webDriver.findElement(changeButton).click();

		//待機時間
		System.out.println(webDriver.getCurrentUrl());
		visibilityTimeout(yesButton, 10);

		//確認ダイアログの認証
		webDriver.findElement(yesButton).click();

		//エビデンス取得（エラー表示）
		getEvidence(new Object() {
		});

		//エラー表示の確認
		WebElement error = webDriver.findElement(By.cssSelector(".help-inline.error"));
		assertTrue(error.isDisplayed());
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 ポリシーに合わない変更パスワードを入力し「変更」ボタン押下")
	void test06() {
		//現在のパスワード入力
		WebElement currentPassElement = webDriver.findElement(currentPass);
		currentPassElement.click();
		currentPassElement.clear();
		currentPassElement.sendKeys("StudentAA02");

		//新しいパスワード入力
		WebElement PassElement = webDriver.findElement(newPass);
		PassElement.click();
		PassElement.clear();
		PassElement.sendKeys("あ".repeat(8));

		//確認パスワード入力
		WebElement confirmPassElement = webDriver.findElement(confirmPass);
		confirmPassElement.click();
		confirmPassElement.clear();
		confirmPassElement.sendKeys("あ".repeat(8));

		//画面下部にスクロール＆待機処理
		scrollTo(down);
		visibilityTimeout(changeButton, 10);

		//「変更」ボタンクリック
		webDriver.findElement(changeButton).click();

		//待機時間
		visibilityTimeout(yesButton, 10);

		//確認ダイアログの認証
		webDriver.findElement(yesButton).click();

		//エビデンス取得（エラー表示）
		getEvidence(new Object() {
		});

		//エラー表示の確認
		WebElement error = webDriver.findElement(By.cssSelector(".help-inline.error"));
		assertTrue(error.isDisplayed());
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 ポリシーに合わない変更パスワードを入力し「変更」ボタン押下(現在と同じパスワード)")
	void test07() {
		//現在のパスワード入力
		WebElement currentPassElement = webDriver.findElement(currentPass);
		currentPassElement.click();
		currentPassElement.clear();
		currentPassElement.sendKeys("StudentAA02");

		//新しいパスワード入力
		WebElement PassElement = webDriver.findElement(newPass);
		PassElement.click();
		PassElement.clear();
		PassElement.sendKeys("StudentAA02");

		//確認パスワード入力
		WebElement confirmPassElement = webDriver.findElement(confirmPass);
		confirmPassElement.click();
		confirmPassElement.clear();
		confirmPassElement.sendKeys("StudentAA02");

		//画面下部にスクロール＆待機処理
		scrollTo(down);
		visibilityTimeout(changeButton, 10);

		//「変更」ボタンクリック
		webDriver.findElement(changeButton).click();

		//待機時間
		visibilityTimeout(yesButton, 10);

		//確認ダイアログの認証
		webDriver.findElement(yesButton).click();

		//エビデンス取得（エラー表示）
		getEvidence(new Object() {
		});

		//エラー表示の確認
		WebElement error = webDriver.findElement(By.cssSelector(".help-inline.error"));
		assertTrue(error.isDisplayed());
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 一致しない確認パスワードを入力し「変更」ボタン押下")
	void test08() {
		//現在のパスワード入力
		WebElement currentPassElement = webDriver.findElement(currentPass);
		currentPassElement.click();
		currentPassElement.clear();
		currentPassElement.sendKeys("StudentAA02");

		//新しいパスワード入力
		WebElement PassElement = webDriver.findElement(newPass);
		PassElement.click();
		PassElement.clear();
		PassElement.sendKeys("StudentAA0202");

		//確認パスワード入力
		WebElement confirmPassElement = webDriver.findElement(confirmPass);
		confirmPassElement.click();
		confirmPassElement.clear();
		confirmPassElement.sendKeys("2222Student");

		//画面下部にスクロール＆待機処理
		scrollTo(down);
		visibilityTimeout(changeButton, 10);

		//「変更」ボタンクリック
		webDriver.findElement(changeButton).click();

		//待機時間
		visibilityTimeout(yesButton, 10);

		//確認ダイアログの認証
		webDriver.findElement(yesButton).click();

		//エビデンス取得（エラー表示）
		getEvidence(new Object() {
		});

		//エラー表示の確認
		WebElement error = webDriver.findElement(By.cssSelector(".help-inline.error"));
		assertTrue(error.isDisplayed());

	}

}
