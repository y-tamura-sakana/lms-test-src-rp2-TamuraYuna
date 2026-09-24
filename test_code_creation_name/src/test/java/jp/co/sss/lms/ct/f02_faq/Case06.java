package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
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
 * ケース06
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース06 カテゴリ検索 正常系")
public class Case06 {

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
	 * test06-01 トップページへアクセスし、ログイン画面が表示される
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
	 * test06-02 ログインに成功する
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

	/**
	 * test06-05 よくある質問画面でカテゴリ検索をし、該当結果を表示させる
	 * */
	@Test
	@Order(5)
	@DisplayName("テスト05 カテゴリ検索で該当カテゴリの検索結果だけ表示")
	void test05() {

		//カテゴリ検索の「研修関係」リンクをクリック
		webDriver.findElement(By.partialLinkText("研修関係")).click();

		//エビデンス取得（カテゴリ検索欄）
		String suffix = "01_該当カテゴリの検索";
		getEvidence(new Object() {
		}, suffix);

		//検索結果までスクロール
		scrollBy("5000");

		//エビデンス取得
		suffix = "02_該当カテゴリの検索結果";
		getEvidence(new Object() {
		}, suffix);

		//検索確認
		String categryUrl = "http://localhost:8080/lms/faq?frequentlyAskedQuestionCategoryId=1";
		assertEquals(categryUrl, webDriver.getCurrentUrl());

		//検索結果箇所指定
		List<WebElement> results = webDriver.findElements(By.cssSelector("tbody tr td dl"));

		//検索結果がnullでないか指定
		assertFalse(results.isEmpty());

		//該当検索結果の確認
		assertEquals("Q.キャンセル料・途中退校について", results.getFirst().getText());
		assertEquals("Q.研修の申し込みはどのようにすれば良いですか？", results.getLast().getText());

	}

	/**
	 * test06-06 よくある質問画面でカテゴリ検索の該当結果の解答を表示させる
	 * */
	@Test
	@Order(6)
	@DisplayName("テスト06 検索結果の質問をクリックしその回答を表示")
	void test06() {

		//カテゴリ検索の「研修関係」リンクをクリック
		webDriver.findElement(By.partialLinkText("研修関係")).click();

		//検索結果までスクロール
		scrollBy("5000");

		//検索結果箇所指定
		List<WebElement> results = webDriver.findElements(By.cssSelector("tbody tr td dl"));

		//各検索結果を開く
		for (WebElement qElement : results) {
			// Q（アコーディオン）をクリックして開く
			qElement.click();

			scrollBy("300");
		}

		String firstAnswer = "A. 受講者の退職や解雇等";

		String lastAnswer = "A. 営業担当がいる場合は、営業担当までご連絡ください。";

		assertTrue(results.getFirst().getText().contains(firstAnswer));
		assertTrue(results.getLast().getText().contains(lastAnswer));

		//エビデンス取得
		getEvidence(new Object() {
		});
	}

}
