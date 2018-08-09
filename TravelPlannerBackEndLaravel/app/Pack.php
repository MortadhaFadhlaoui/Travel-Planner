<?php

namespace App;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Notifications\Notifiable;
class Pack extends Model
{
    use Notifiable;

    /**
     * The attributes that are mass assignable.
     *
     * @var array
     */
    protected $fillable = [
        'date_debut','date_fin','nom_depart','prix',
    ];
      public function payss()
    {
        return $this->hasMany('App\Pays');
    }
    public function dayplans()
    {
        return $this->hasMany('App\Dayplan');
    }
}
