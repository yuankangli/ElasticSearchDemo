package com.elastic.controller;

import com.elastic.entity.Poetry;
import com.elastic.init.Init;
import com.elastic.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.elasticsearch.index.query.QueryBuilders.*;

@RestController
@RequestMapping("/es")
public class EsController {

	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private Init init;

	@RequestMapping("/regenerate")
	public String regenerate(Integer total) {
		if (total == null) {
			total = 100000;
		}
		long l = System.currentTimeMillis();
		init.init(total);
		String info = String.format("regenerate %s rows data success: %s s",
				total, (System.currentTimeMillis() - l) / 1000);
		System.out.println(info);
		return info;
	}

	/**
	 * 单字符串模糊查询，默认排序。将从所有字段中查找包含传来的word分词后字符串的数据集
	 * http://localhost:8990/es/singleWord?word=浣溪沙&size=20&page=0
	 * <p>
	 * 共13个结果，按照默认的排序方式，即匹配相关度排序，前10个匹配度最高，都是完全带“浣溪沙”三个字的。
	 * 第10个、11个都是题目和正文都包含“溪”字而且出现了2次“溪”，最后一个是正文带一个“溪”。
	 * <p>
	 * 注意: es 分页机制问题, 例如查询第10000-10010的数据, 它会查询出前10010的数据, 然后舍弃前10000笔数据
	 * 此时会报错: QueryPhaseExecutionException[Result window is too large, from + size must be less than or equal to: [10000] but was [10010]
	 * 解决办法如下:
	 * 1. 调大 max_result_window 配置, 治标不治本, 不推荐
	 * 2. 一般而言, 我们不需要查询那么多数据, 直接在代码管控查询数据<100000
	 * 详情请参考:
	 * https://blog.csdn.net/zzh920625/article/details/83929843
	 * https://blog.csdn.net/Arvinzr/article/details/79228944?utm_medium=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromMachineLearnPai2-1.nonecase&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromMachineLearnPai2-1.nonecase
	 */
	@RequestMapping("/singleWord")
	public Object singleTitle(String word, @PageableDefault Pageable pageable) {
		//使用queryStringQuery完成单字符串查询
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(queryStringQuery(word)).withPageable(pageable).build();
		return elasticsearchTemplate.queryForList(searchQuery, Poetry.class);
	}

	/**
	 * 单字符串模糊查询，单字段排序。
	 * http://localhost:8990/es/singleWord1?word=浣溪沙&size=20
	 */
	@RequestMapping("/singleWord1")
	public Object singlePost(String word, @PageableDefault(sort = "weight", direction = Sort.Direction.DESC) Pageable pageable) {
		//使用queryStringQuery完成单字符串查询
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(queryStringQuery(word)).withPageable(pageable).build();
		return elasticsearchTemplate.queryForList(searchQuery, Poetry.class);
	}

	/**
	 * 单字段对某字符串模糊查询
	 * http://localhost:8990/es/singleMatch?content=落日熔金&size=20
	 * http://localhost:8990/es/singleMatch?userId=1&size=20
	 */
	@RequestMapping("/singleMatch")
	public Object singleMatch(String content, Integer userId, @PageableDefault Pageable pageable) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(matchQuery("content", content))
				.withQuery(matchQuery("userId", userId)).withPageable(pageable).build();
		return elasticsearchTemplate.queryForList(searchQuery, Poetry.class);
	}

	/**
	 * 单字段对某短语进行匹配查询，短语分词的顺序会影响结果
	 * http://localhost:8990/es/singleMatch?content=落日熔金&size=20
	 */
	@RequestMapping("/singlePhraseMatch")
	public Object singlePhraseMatch(String content, @PageableDefault Pageable pageable) {
		SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(matchPhraseQuery("content", content)).withPageable(pageable).build();
		return elasticsearchTemplate.queryForList(searchQuery, Poetry.class);
	}

}
