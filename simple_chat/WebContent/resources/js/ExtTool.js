var ExtTool = {
	items : {
		zip : [ 'RAR', 'ZIP', '7Z', 'GZ', 'BZ', 'BZ2', 'XZ', 'ACE', 'UHA',
				'UDA', 'ZPAQ', 'TAR', 'WIM', 'LZH' ],
		video : [ 'asf', 'wm', 'wmp', 'wmv', 'ram', 'rm', 'rmvb', 'rpm', 'scm',
				'rp', 'dat', 'evo', 'vob', 'ifo', 'mov', 'qt', '3g2', '3gp',
				'3gp2', '3gpp', 'bhd', 'ghd', 'amv', 'avi', 'bik', 'csf',
				'd2v', 'dsm', 'ivf', 'm1v', 'm2p', 'm2ts', 'm2v', 'm4b', 'm4p',
				'm4v', 'mkv', 'mp4', 'mpe', 'mpeg', 'mpg', 'mts', 'ogm', 'pmp',
				'pmp2', 'pss', 'pva', 'ratDVD', 'smk', 'tp', 'tpr', 'ts',
				'vg2', 'vid', 'vp6', 'vp7', 'wv', 'asm', 'avsts', 'divx',
				'webm', 'flv' ],
		music : [ 'wma', 'ra', 'aif', 'aiff', 'amr', 'aac', 'ac3', 'acc',
				'act', 'ape', 'au', 'cda', 'dts', 'flac', 'm4a', 'mac', 'mid',
				'midi', 'mp2', 'mp3', 'mp5', 'mpa', 'mpga', 'mod', 'ogg',
				'ofr', 'rmi', 'tak', 'tta', 'wav', 'aifc' ],
		flash : [ 'fla', 'swf' ],
		pdf : [ 'pdf' ],
		image : [ 'BMP', 'ICO', 'JPG', 'JNG', 'KOA', 'IFF', 'MNG', 'PCD',
				'PCX', 'PNG', 'RAS', 'TGA', 'TIF', 'WAP', 'PSD', 'CUT', 'XBM',
				'XPM', 'DDS', 'GIF', 'HDR', 'G3', 'SGI', 'EXR', 'J2K', 'JP2',
				'PFM', 'PCT', 'RAW', 'WMF', 'JPC', 'PGX', 'PNM', 'SKA', 'jpeg' ],
		txt : [ 'txt', 'lrc', 'xml', 'ini', 'log', 'doc', 'rtf', 'htm', 'asp',
				'c', 'bas', 'prg', 'cmd' ],
		exe : [ 'exe', 'bat' ]
	},
	getClassName : function(name) {
		name = name.toLowerCase();
		for ( var prop in this.items) {
			var arr = this.items[prop];
			for ( var i in arr) {
				if (eval("/\\." + arr[i].toLowerCase() + "$/").test(name)) {
					return prop;
				}
			}
		}
		return 'unknown';
	}
};