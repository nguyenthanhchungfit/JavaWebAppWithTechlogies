namespace java models
include "referencesobj.thrift"
struct User{
	1: required string username,
	2: string password,
	3: string name,
	4: string dob,
	5: string address,
	6: string phone,
	7: bool sex,
	8: list<referencesobj.Referencer> favor_list_songs,
	9: list<referencesobj.Referencer> favor_list_singers,
	10: string image
}

struct UserResult{
	1: required i16 result = 0,
	2: optional User user
}
