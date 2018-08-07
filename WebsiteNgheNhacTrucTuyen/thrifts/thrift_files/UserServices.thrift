namespace java thrift_services
include "Customer.thrift"

service UserServices{
	string login(1: string username, 2: string password),
	bool signup(1: Customer.Customer customer),
	i64 getTotalNumberUsers(),
	void logout(1: string c_user),
	bool isAdminSession(1: string c_user)
}
