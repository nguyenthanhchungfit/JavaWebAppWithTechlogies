namespace java models

struct DataComment{
	1: string username,
	2: string date,
	3: string content
}

struct Comment{
	1: required string id,
	2: list<DataComment> datas
}

struct CommentResult{
	1: required i16 result = 0,
	2: optional Comment comment
}
