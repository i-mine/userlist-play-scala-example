package JsonDemo

import play.api.libs.json.Json
/**
  * author: dulei
  * date: 18-4-2
  * desc: JsonObject parse string
  */
object JsonParseDemo extends App {
  val jsonString: String = """{
                                "login": "pedrorijo91",
                                "id": 1999050,
                                "avatar_url": "https://avatars.githubusercontent.com/u/1999050?v=3",
                                "gravatar_id": "",
                                "url": "https://api.github.com/users/pedrorijo91",
                                "html_url": "https://github.com/pedrorijo91",
                                "followers_url": "https://api.github.com/users/pedrorijo91/followers",
                                "following_url": "https://api.github.com/users/pedrorijo91/following{/other_user}",
                                "gists_url": "https://api.github.com/users/pedrorijo91/gists{/gist_id}",
                                "starred_url": "https://api.github.com/users/pedrorijo91/starred{/owner}{/repo}",
                                "subscriptions_url": "https://api.github.com/users/pedrorijo91/subscriptions",
                                "organizations_url": "https://api.github.com/users/pedrorijo91/orgs",
                                "repos_url": "https://api.github.com/users/pedrorijo91/repos",
                                "events_url": "https://api.github.com/users/pedrorijo91/events{/privacy}",
                                "received_events_url": "https://api.github.com/users/pedrorijo91/received_events",
                                "type": "User",
                                "site_admin": false,
                                "name": "Pedro Rijo",
                                "company": "Codacy",
                                "blog": "http://pedrorijo.com/",
                                "location": "Lisbon, Portugal",
                                "email": null,
                                "hireable": true,
                                "bio": null,
                                "public_repos": 35,
                                "public_gists": 4,
                                "followers": 23,
                                "following": 62,
                                "created_at": "2012-07-18T14:34:35Z",
                                "updated_at": "2016-01-05T00:02:37Z"
                              }"""
  val jsonObject = Json.parse(jsonString)
  val login = jsonObject \ "login"
  println(login.as[String])

}
