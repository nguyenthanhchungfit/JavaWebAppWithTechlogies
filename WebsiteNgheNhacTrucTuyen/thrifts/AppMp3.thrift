namespace java models


struct Singer{
	1: required string id,
	2: string name,
	3: string realname,
	4: string dob,
	5: string country,
	6: string description	
}

struct SingerResult{
	1: required i16 result = 0,
	2: optional Singer singer
}

struct Song{
	1: string id,
	2: string name,
	3: string album,
	4: list<string> lyrics,
	5: list<string> composers,
	6: list<string> kinds,
	7: list<Singer> singers
	
}

struct SongResult{
	1: required i16 result = 0,
	2: optional Song song
}

service ServicesDataCenter{
	SongResult getSongData(1: string name_song),
	list<Song> getSongsData(1: string name_song),
	SingerResult getSingerData(1: string id_singer),
	string getLyric(String id_song, String page)
}

