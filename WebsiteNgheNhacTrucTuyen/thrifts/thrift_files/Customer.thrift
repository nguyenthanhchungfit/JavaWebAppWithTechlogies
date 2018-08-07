namespace java models

struct Customer{
	1: required string username,
	2: string password,
	3: string fullname,
	4: string dob,
	5: string address,
	6: bool sex,
	7: string email,
	8: string image,
	9: string createDate,
	10: string updateDate
}

struct CustomerResult{
	1: required i16 result = 0,
	2: optional Customer customer
}

