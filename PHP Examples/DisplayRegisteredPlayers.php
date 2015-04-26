<html>
	<body>
		<?php
			/*
			*	Copyright Niels Hamelink
			* 	26-4-2015
			*/

			/*
			*	Config parts
			*/
			$_config['host']			=	'127.0.0.1';
			$_config['username']		=	'Root';
			$_config['password']		=	'Password';
			$_config['database']		=	'Database';
			$_config['table_name']		=	'users';
			$_config['username_column']	=	'Username';

			// Creatings a (PDO) MySQL connections
			// You can replace it with:
			// $pdo = mysqli_connect(...) if you prefer mysqli above using PDO
			// Don't forget to remove the exception around the connecting
			try {
				$pdo = new PDO ('mysql:host=' . $_config['host'] . ';dbname=' . $_config['database'], $_config['username'], $_config['password']);
			}
			catch (PDOException $ex) {
				die ('Could not connect with the database!');
			}
			
			// Unsettings the connection parts of the config, for better safety / speed performence.
			for($i = 1; $i < count($_config); $i++){
				if($_conig[$i] != $_config['table_name']
					&& $_config[$i] != $_config['username_column'])
					unset($_config[$i]);
			}
			
			// Sellect query (Get all items from the database)
			$query = $pdo->query('SELECT * FROM ' . $_config['table_name']);
			// Run them 1 by 1
			while($row = $query->fetch()){
				// Set the table variable
				$table = '<tr><td>' . $row[$_config['username_column']] . '</tr></td>';
			}
		?>	
		
		<!-- Start Table -->
		<table>
			<tr><td><strong>Username</strong></td></tr>
			<?=$table;?>
		</table>
		<!-- End Table -->
	</body>
</html>