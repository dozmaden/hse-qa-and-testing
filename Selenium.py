import unittest
from time import sleep

from selenium import webdriver
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.common.by import By

import datetime

# Ozmaden Deniz BSE-196

class SeleniumTests(unittest.TestCase):    
    def __init__(self, *args, **kwargs):
        super(SeleniumTests, self).__init__(*args, **kwargs)

        self.driver = webdriver.Chrome(ChromeDriverManager().install())

        # константы
        self.ORIGINAL_USERNAME = "dozmaden"
        self.ORIGINAL_PASSWORD = "&#B!!elt3R8CayhgTcg$ZeMt"

        self.SECOND_USERNAME = "ozmadeniz"
        self.SECOND_PASSWORD = "eVriHlHmPNkf*#TMjhWhup72"

        self.LOGIN_URL = "https://ruswizard.su/test/wp-login.php"
        self.LIST_URL = "https://ruswizard.su/test/wp-admin/edit.php?post_type=post"
        self.NEW_POST_URL = "https://ruswizard.su/test/wp-admin/post-new.php"

        self.TEST_TEXT = "THIS IS A TOTALLY LEGIT POST BY A REAL HUMAN BEING."
        self.TEST_COMMENT = "THIS IS A TOTALLY A NORMAL COMMENT MADE BY ANOTHER HUMAN."

        self.login = "user_login"
        self.password = "user_pass"
        self.submit = "wp-submit"

        self.title = "post-title-0"
        self.find_publish_button =  "//*[contains(text(), 'Опубликовать')]"
        self.publish_button = "editor-post-publish-button"

        self.in_list = "wp-admin-bar-edit"
        self.destroy = "is-destructive"

        self.form_comment = "comment-form-comment"
        self.comment_submit = "form-submit"
        self.comment = "comment"
        self.submit_comment = "submit"
        self.comment_script = "arguments[0].click();"

        self.toggle_schedule = "edit-post-post-schedule__toggle"
        self.change_minutes = "components-datetime__time-field-minutes-input"
        self.submit_date = "components-datetime__time"
        
    # возможность добавления записи
    # доступность сохранённого фрагмента через список записей данного пользователя;
    def test_post(self):
        # Пользователь заходит на сайт, создаёт пост и проверяет его существование.
        
        driver = self.driver
        driver.maximize_window()

        # заходим на сайт через ориганального пользователя
        driver.get(self.LOGIN_URL)
        driver.find_element(By.ID, self.login).send_keys(self.ORIGINAL_USERNAME)
        driver.find_element(By.ID, self.password).send_keys(self.ORIGINAL_PASSWORD)
        driver.find_element(By.ID, self.submit).click()
        
        # создаём новый пост
        driver.get(self.NEW_POST_URL)
        driver.find_element(By.ID, self.title).send_keys(self.TEST_TEXT)
        driver.find_element(By.XPATH, self.find_publish_button).click()
        driver.find_element(By.CLASS_NAME, self.publish_button).click()

        sleep(5)

        published_post = driver.find_element(By.ID, 'inspector-text-control-0').get_attribute("value")

        driver.get(published_post)
        assert self.TEST_TEXT in driver.page_source

        driver.quit()

    # возможность для создавшего пользователя удалить запись;
    def test_delete(self):
        driver = self.driver
        driver.maximize_window()

        # аналогично предыдущему тесты, пользователь создаёт пост
        driver.get(self.LOGIN_URL)
        driver.find_element(By.ID, self.login).send_keys(self.ORIGINAL_USERNAME)
        driver.find_element(By.ID, self.password).send_keys(self.ORIGINAL_PASSWORD)
        driver.find_element(By.ID, self.submit).click()

        driver.get(self.NEW_POST_URL)
        driver.find_element(By.ID, self.title).send_keys(self.TEST_TEXT)
        driver.find_element(By.XPATH, self.find_publish_button).click()
        driver.find_element(By.CLASS_NAME, self.publish_button).click()

        sleep(5)

        # удаляем пост
        published_post = driver.find_element(By.ID, 'inspector-text-control-0').get_attribute("value")
        driver.get(published_post)
        driver.find_element(By.ID, self.in_list).click()

        sleep(10)
        driver.find_element(By.CLASS_NAME, self.destroy).click()

        # теперь заходим как второй пользователь, для того чтобы удостоверитсья что пост действительно удалён для всех
        driver.get(self.LOGIN_URL)
        driver.find_element(By.ID, self.login).clear()
        driver.find_element(By.ID, self.login).send_keys(self.SECOND_USERNAME)
        driver.find_element(By.ID, self.password).send_keys(self.SECOND_PASSWORD)
        driver.find_element(By.ID, self.submit).click()

        driver.get(published_post)
        not_found = "Страница не найдена"
        assert not_found in driver.page_source

        driver.quit()

    # создание записи с отложенной публикацией (минуты и менее достаточно);
    def test_delay(self):
        # Пользователь заходит на сайт, создаёт пост с отложенной публикацией и проверяет его существование.
        driver = self.driver
        driver.maximize_window()

        driver.get(self.LOGIN_URL)
        driver.find_element(By.ID, self.login).send_keys(self.ORIGINAL_USERNAME)
        driver.find_element(By.ID, self.password).send_keys(self.ORIGINAL_PASSWORD)
        driver.find_element(By.ID, self.submit).click()

        driver.get(self.NEW_POST_URL)
        driver.find_element(By.ID, self.title).send_keys(self.TEST_TEXT)

        driver.find_element(By.CLASS_NAME, self.toggle_schedule).click()

        # обнуляем таймер и ставим время через минуту
        driver.execute_script('arguments[0].value = "";', driver.find_element(By.CLASS_NAME, self.change_minutes))
        driver.find_element(By.CLASS_NAME, self.change_minutes).send_keys(datetime.datetime.now().minute + 1)
        sleep(10)
        driver.find_element(By.CLASS_NAME, self.submit_date).click()

        driver.find_element(By.XPATH, self.find_publish_button).click()
        driver.find_element(By.CLASS_NAME, self.publish_button).click()

        sleep(70)

        assert self.TEST_TEXT in driver.page_source
        driver.quit()

    def test_comment(self):
        # Пользователь 1 создаёт пост, пользователь 2 оставляет комментарий. 
        driver = self.driver
        driver.maximize_window()
        driver.get(self.LOGIN_URL)
        driver.find_element(By.ID, self.login).send_keys(self.ORIGINAL_USERNAME)
        driver.find_element(By.ID, self.password).send_keys(self.ORIGINAL_PASSWORD)
        driver.find_element(By.ID, self.submit).click()

        driver.get(self.NEW_POST_URL)
        driver.find_element(By.ID, self.title).send_keys(self.TEST_TEXT)
        driver.find_element(By.XPATH, self.find_publish_button).click()
        driver.find_element(By.CLASS_NAME, self.publish_button).click()

        sleep(5)

        published_post = driver.find_element(By.ID, 'inspector-text-control-0').get_attribute("value")

        driver.get(self.LOGIN_URL)
        driver.find_element(By.ID, self.login).clear()
        driver.find_element(By.ID, self.login).send_keys(self.SECOND_USERNAME)
        driver.find_element(By.ID, self.password).send_keys(self.SECOND_PASSWORD)
        driver.find_element(By.ID, self.submit).click()

        driver.get(published_post)

        driver.find_element(By.CLASS_NAME,self.form_comment).find_element(By.ID, self.comment).send_keys(self.TEST_COMMENT)
        driver.execute_script(self.comment_script, driver.find_element(By.CLASS_NAME, self.comment_submit).find_element(By.ID, self.submit_comment))

        driver.get(published_post)
        assert self.TEST_COMMENT in driver.page_source

        driver.quit()

if __name__ == '__main__':
    unittest.main()