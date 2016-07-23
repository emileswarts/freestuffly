(ns freestuffly.test.gumtree.result-parser
  (:require [clojure.test :refer :all]
            [freestuffly.gumtree.result-parser :refer :all]))

(deftest scraped-content-test
  (testing "Parse content"
    (let [html "<table id='group_posts_table'>
                  <tr>
                    <td>
                      <a href='https://foo.com'>
                        <span class='post_icon'><span>&gt;</span> OFFER</span>
                      </a>
                      <br /> Mon Jul 11 08:55:58 2016<br />
                      (#55415446)
                    </td>
                    <td>
                      <a href='https://foo.com'>Spaypaint</a> (Off East Street Old Kent Road end)<br />
                      <p class='textCenter'><a href='https://foo.com'>See details</a></p>
                    </td>
                  </tr>
                  <tr class='candy1'>
                    <td>
                      <a href='https://bar.com' class='noDecoration'><span>
                      <span>&gt;</span> OFFER</span>
                      </a>
                      <br /> Sun Jul 10 22:48:32 2016<br />
                      (#55410662)
                    </td>
                    <td>
                      <a href='https://bar.com'>
                        Shelving unit
                      </a>
                      (london, SE1) <br />
                      <p class='textCenter'>
                        <a href='https://bar.com'>See details</a>
                      </p>
                    </td>
                  </tr>
               </table>"
          expected-result "FOUND\n\n\n\n({:href \"https://foo.com\"} [\"Spaypaint\"])"]
      (is (= expected-result (parsed html))))))
