<?php

namespace App;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Notifications\Notifiable;
class Pays extends Model
{
     use Notifiable;

    /**
     * The attributes that are mass assignable.
     *
     * @var array
     */
    protected $fillable = [
        'nom_pays','etat','pack_id',
    ];
    public function pack()
    {
        return $this->belongsTo('App\Pack');
    }
}
