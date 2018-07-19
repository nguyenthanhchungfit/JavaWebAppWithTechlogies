namespace java thrift_services
include "Customer.thrift"

service UserServices{
	string login(1: string username, 2: string password),
	bool signup(1: Customer.Customer customer),
	i64 getTotalNumberUsers()
}
